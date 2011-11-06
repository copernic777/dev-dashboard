package dmnlukasik.sonar.dashboard

import org.sonar.wsclient.connectors.HttpClient4Connector
import org.sonar.wsclient.{Host, Sonar}
import org.sonar.wsclient.services.ResourceQuery

object Main extends App {
  val SONAR_PROJECT_ID = 48569
  val SONAR_PROJECT_RESOURCE_KEY = "org.codehaus.sonar:sonar"
  val NEMO_SONAR_HOST = "http://nemo.sonarsource.org"

  val sonar = new Sonar(new HttpClient4Connector(new Host(NEMO_SONAR_HOST)))
  val resource = sonar.find(ResourceQuery.createForMetrics(SONAR_PROJECT_ID.toString, "coverage"))
  println(resource.getMeasures)
}
