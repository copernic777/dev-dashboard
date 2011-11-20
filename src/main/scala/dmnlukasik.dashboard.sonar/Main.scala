package dmnlukasik.dashboard.sonar

import scala.collection.JavaConversions._
import org.sonar.wsclient.connectors.HttpClient4Connector
import org.sonar.wsclient.{Host, Sonar}
import org.scala_tools.time.Imports._
import org.sonar.wsclient.services.TimeMachineQuery
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._
import com.mongodb.casbah.{MongoCollection, MongoDB, MongoConnection}

object Main extends App {
  RegisterJodaTimeConversionHelpers()

  val SONAR_PROJECT_ID = 48569
  val SONAR_PROJECT_RESOURCE_KEY = "org.codehaus.sonar:sonar"
  val NEMO_SONAR_HOST = "http://nemo.sonarsource.org"

  val sonar = new Sonar(new HttpClient4Connector(new Host(NEMO_SONAR_HOST)))
  val timeMachineQuery: TimeMachineQuery = TimeMachineQuery.createForMetrics(SONAR_PROJECT_ID.toString, "coverage")
  timeMachineQuery.setFrom(new DateTime(2011, 7, 1, 0, 0, 0, 0).toDate)
  timeMachineQuery.setTo(new DateTime(2011, 12, 31, 0, 0, 0, 0).toDate)
  val resource = sonar.find(timeMachineQuery)

  resource.getCells().foreach(c => println(c.getDate + " " + c.getValues()(0)))

  val mongoConnection = MongoConnection()
  val mongoDB = mongoConnection("dev-dashboard")
  val metrics: MongoCollection = mongoDB("metrics.coverage")

  resource.getCells().foreach(c =>
    metrics += MongoDBObject("date" -> c.getDate, "value" -> c.getValues()(0)))

  println(metrics.find.mkString("\n"))
}