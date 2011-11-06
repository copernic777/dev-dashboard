package dmnlukasik.sonar.dashboard

import org.sonar.wsclient.connectors.HttpClient4Connector
import org.sonar.wsclient.{Host, Sonar}
import org.sonar.wsclient.services.ResourceQuery

object Main extends App {

  val sonar = new Sonar(new HttpClient4Connector(new Host("http://nemo.sonarsource.org")))
  val metrics = sonar.find(ResourceQuery.createForMetrics("48569", "coverage", "lines", "violations"))
  println(metrics.getMeasure("coverage"))
}
