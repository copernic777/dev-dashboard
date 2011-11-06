package dmnlukasik.sonar.dashboard

import org.sonar.wsclient.connectors.HttpClient4Connector
import org.sonar.wsclient.{Host, Sonar}
import org.sonar.wsclient.services.TimeMachineQuery
import org.scala_tools.time.Imports._

object Main extends App {
  val SONAR_PROJECT_ID = 48569
  val SONAR_PROJECT_RESOURCE_KEY = "org.codehaus.sonar:sonar"
  val NEMO_SONAR_HOST = "http://nemo.sonarsource.org"

  val sonar = new Sonar(new HttpClient4Connector(new Host(NEMO_SONAR_HOST)))
  val timeMachineQuery: TimeMachineQuery = TimeMachineQuery.createForMetrics(SONAR_PROJECT_ID.toString, "coverage", "complexity")
  timeMachineQuery.setFrom(new DateTime(2011, 11, 6, 0, 0, 0, 0).toDate)
  val resource = sonar.find(timeMachineQuery)

  resource.getCells.foreach(c => println(c.getValues.deep.toString()))
  resource.getColumns.foreach(c =>
    println("metricKey: " + c.getMetricKey +
      ", index: " + c.getIndex +
      ", characteristicKey: " + c.getCharacteristicKey +
      ", modelName: " + c.getModelName +
      ", date: " + resource.getCells()(0).getDate) +
      ", value: " + resource.getCells()(0).getValues()(c.getIndex))
}
