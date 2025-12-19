package com.alveteg.simon.workouts.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.color
import androidx.graphics.shapes.pill
import com.alveteg.simon.workouts.ui.ExerciseWrapper
import com.alveteg.simon.workouts.ui.SessionWrapper
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.point
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.m3.R
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.text.format

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
    setHistory.filter { it.second.sets.isNotEmpty() }
  }
  LaunchedEffect(filteredSetHistory) {
    if (filteredSetHistory.isNotEmpty()) {
      modelProducer.runTransaction {
        val weights = filteredSetHistory.map {
          it.second.sets.maxByOrNull { it.weight ?: 0f }?.weight ?: 0f
        }
        val dates = filteredSetHistory.map {
          it.first.session.start.toLocalDate().toEpochDay()
        }
        lineSeries {
          series(x = dates, y = weights)
        }
      }
    }
  }

  val lazyRowState = rememberLazyListState()
  var selectedSessionIndex by remember { mutableStateOf(0) }

  LaunchedEffect(selectedSessionIndex) {
    Timber.d("Selected session index: $selectedSessionIndex, size: ${filteredSetHistory.size}")
    lazyRowState.animateScrollToItem(selectedSessionIndex)
  }

  val hapticFeedback = LocalHapticFeedback.current
  val markerVisibilityListener = remember(filteredSetHistory) {
    object : CartesianMarkerVisibilityListener {

      private var xTarget = -1L

      override fun onUpdated(marker: CartesianMarker, targets: List<CartesianMarker.Target>) {
        val target = targets.firstOrNull() as? LineCartesianLayerMarkerTarget ?: return
        val markerIndex = target.points.last().entry.x.toLong()
        Timber.d("Selected marker: $markerIndex")
        xTarget = markerIndex
        val temp = filteredSetHistory.indexOfFirst {
          val day = it.first.session.start.toLocalDate().toEpochDay()
          Timber.d("day: $day, markerIndex: $markerIndex")
          day == markerIndex
        }
        if (temp != -1) selectedSessionIndex = temp
        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
      }

      override fun onShown(marker: CartesianMarker, targets: List<CartesianMarker.Target>) {
        onUpdated(marker, targets)
      }

      override fun onHidden(marker: CartesianMarker) {
        xTarget = -1
      }
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
                pointProvider = LineCartesianLayer.PointProvider.single(
                  LineCartesianLayer.point(
                    rememberShapeComponent(
                      fill = fill(MaterialTheme.colorScheme.primary),
                      shape = CorneredShape.Pill
                    ),
                    size = 6.dp
                  )
                )
              )
            )
          ),
          startAxis = VerticalAxis.rememberStart(),
          bottomAxis = HorizontalAxis.rememberBottom(
            valueFormatter = bottomAxisValueFormatter,
          ),
          marker = rememberDefaultCartesianMarker(
            label = rememberTextComponent(),
            valueFormatter = DefaultCartesianMarker.ValueFormatter.default()
          ),
          markerVisibilityListener = markerVisibilityListener,
        ),
        scrollState = rememberVicoScrollState(scrollEnabled = false),
        modelProducer = modelProducer,
        consumeMoveEvents = true
      )
    }


    LazyRow(
      state = lazyRowState,
      reverseLayout = true,
      modifier = modifier
        .padding(vertical = 4.dp)
        .height(60.dp)
    ) {
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
      if (filteredSetHistory.isEmpty()) {
        item {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
              .fillMaxHeight()
              .padding(bottom = 8.dp)
              .width(LocalWindowInfo.current.containerSize.width.dp)
          ) {
            Text(
              text = "No history available.",
              style = MaterialTheme.typography.titleMediumEmphasized,
            )
            Text(
              text = "Previous sessions will show up here.",
              style = MaterialTheme.typography.labelMedium
            )
          }
        }
      }
    }
  }
}