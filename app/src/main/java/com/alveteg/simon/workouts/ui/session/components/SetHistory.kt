package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.alveteg.simon.workouts.utils.FadeInVisibility
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.point
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SetHistory(
  setHistory: List<Pair<SessionWrapper, ExerciseWrapper>>,
  modifier: Modifier = Modifier
) {
  val modelProducer = remember { CartesianChartModelProducer() }
  val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM yyyy") }
  val bottomAxisValueFormatter = CartesianValueFormatter { _, x, _ ->
    LocalDate.ofEpochDay(x.toLong()).format(dateFormatter)
  }

  val filteredSetHistory = remember(setHistory) {
    setHistory.filter { it.second.sets.isNotEmpty() && !it.second.sets.any { it.reps == null } }
  }

  val dates = remember(filteredSetHistory) {
    filteredSetHistory.map { it.first.session.start.toLocalDate().toEpochDay().toDouble() }
  }
  val weights = remember(filteredSetHistory) {
    filteredSetHistory.map { it.second.sets.maxByOrNull { set -> set.weight ?: 0f }?.weight ?: 0f }
  }

  LaunchedEffect(filteredSetHistory) {
    if (filteredSetHistory.isNotEmpty()) {
      modelProducer.runTransaction {
        lineSeries {
          series(x = dates, y = weights)
        }
      }
    }
  }

  var showPlaceholder by remember { mutableStateOf(false) }
  LaunchedEffect(filteredSetHistory) {
    if (filteredSetHistory.isEmpty()) {
      delay(300)
      showPlaceholder = true
    } else {
      showPlaceholder = false
    }
  }

  val lazyRowState = rememberLazyListState()
  var selectedSessionIndex by remember { mutableStateOf(0) }

  var isProgrammaticScroll by remember { mutableStateOf(false) }

  val scrollCenterIndex by remember {
    derivedStateOf {
      val layoutInfo = lazyRowState.layoutInfo
      val visibleItemsInfo = layoutInfo.visibleItemsInfo
      if (visibleItemsInfo.isEmpty()) {
        0
      } else {
        val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
        visibleItemsInfo.minByOrNull { abs((it.offset + it.size / 2) - viewportCenter) }?.index
          ?: visibleItemsInfo.first().index
      }
    }
  }

  val isDragged by lazyRowState.interactionSource.collectIsDraggedAsState()

  LaunchedEffect(scrollCenterIndex) {
    if (filteredSetHistory.isNotEmpty() && (isDragged || (lazyRowState.isScrollInProgress && !isProgrammaticScroll))) {
      selectedSessionIndex = scrollCenterIndex
    }
  }

  LaunchedEffect(selectedSessionIndex) {
    if (filteredSetHistory.isNotEmpty() && !lazyRowState.isScrollInProgress) {
      isProgrammaticScroll = true
      lazyRowState.animateScrollToItem(selectedSessionIndex)
      isProgrammaticScroll = false
    }
  }

  LaunchedEffect(lazyRowState.isScrollInProgress) {
    if (!lazyRowState.isScrollInProgress) {
      isProgrammaticScroll = false
    }
  }

  val hapticFeedback = LocalHapticFeedback.current
  val markerVisibilityListener = remember(filteredSetHistory) {
    object : CartesianMarkerVisibilityListener {
      override fun onUpdated(marker: CartesianMarker, targets: List<CartesianMarker.Target>) {
        val target = targets.firstOrNull() as? LineCartesianLayerMarkerTarget ?: return
        val markerX = target.points.last().entry.x.toLong()

        val index = filteredSetHistory.indexOfFirst {
          it.first.session.start.toLocalDate().toEpochDay() == markerX
        }

        if (index != -1 && index != selectedSessionIndex) {
          selectedSessionIndex = index
          hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
        }
      }

      override fun onShown(marker: CartesianMarker, targets: List<CartesianMarker.Target>) {
        onUpdated(marker, targets)
      }

      override fun onHidden(marker: CartesianMarker) {}
    }
  }

  val defaultPoint = LineCartesianLayer.point(
    rememberShapeComponent(fill(MaterialTheme.colorScheme.primary), CorneredShape.Pill),
    size = 6.dp
  )
  val highlightedPoint = LineCartesianLayer.point(
    rememberShapeComponent(fill(MaterialTheme.colorScheme.primary), CorneredShape.Pill),
    size = 12.dp
  )

  val pointProvider = remember(selectedSessionIndex, dates) {
    object : LineCartesianLayer.PointProvider {
      override fun getPoint(
        entry: LineCartesianLayerModel.Entry,
        seriesIndex: Int,
        extraStore: ExtraStore
      ): LineCartesianLayer.Point {
        val entryIndex = dates.indexOf(entry.x)
        return if (entryIndex == selectedSessionIndex) highlightedPoint else defaultPoint
      }

      override fun getLargestPoint(extraStore: ExtraStore): LineCartesianLayer.Point =
        highlightedPoint
    }
  }

  Column {
    ProvideVicoTheme(rememberM3VicoTheme()) {
      CartesianChartHost(
        chart = rememberCartesianChart(
          rememberLineCartesianLayer(
            LineCartesianLayer.LineProvider.series(
              LineCartesianLayer.rememberLine(
                fill = LineCartesianLayer.LineFill.single(fill(MaterialTheme.colorScheme.primary)),
                areaFill = null,
                pointProvider = pointProvider
              )
            )
          ),
          startAxis = VerticalAxis.rememberStart(),
          bottomAxis = HorizontalAxis.rememberBottom(
            valueFormatter = bottomAxisValueFormatter,
          ),
          marker = rememberDefaultCartesianMarker(
            label = rememberTextComponent(),
            valueFormatter = DefaultCartesianMarker.ValueFormatter.default(),
            indicator = { color -> shapeComponent(fill = fill(color), shape = CorneredShape.Pill) },
            indicatorSize = 12.dp
          ),
          markerVisibilityListener = markerVisibilityListener,
        ),
        scrollState = rememberVicoScrollState(scrollEnabled = false),
        modelProducer = modelProducer,
        animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
        consumeMoveEvents = true,
        placeholder = {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
          ) {
            FadeInVisibility(showPlaceholder) {
              Text(
                text = "No history available.",
                style = MaterialTheme.typography.titleMediumEmphasized,
              )
            }
          }
        },
        modifier = Modifier.padding(horizontal = 8.dp)
      )
    }
    LazyRow(
      state = lazyRowState,
      reverseLayout = true,
      modifier = modifier
        .padding(vertical = 4.dp)
        .height(60.dp)
    ) {
      if (filteredSetHistory.isEmpty()) {
        item {
          FadeInVisibility(showPlaceholder) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center,
              modifier = Modifier
                .fillParentMaxWidth()
                .height(60.dp)
                .padding(bottom = 8.dp)
            ) {
              Text(
                text = "Previous sessions will show up here.",
                style = MaterialTheme.typography.labelMedium
              )
            }
          }
        }
      }
      items(filteredSetHistory) { pair ->
        val (sessionWrapper, exerciseWrapper) = pair
        SetHistoryCard(
          modifier = Modifier
            .padding(horizontal = 4.dp)
            .animateItem(),
          sessionWrapper = sessionWrapper,
          exerciseWrapper = exerciseWrapper
        )
      }
    }
  }
}

