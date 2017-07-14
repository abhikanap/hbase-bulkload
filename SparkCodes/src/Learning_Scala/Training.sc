import org.apache.spark.graphx._
import org.apache.phoenix.spark._
val rdd = sc.phoenixTableAsRDD("EMAIL_ENRON", Seq("MAIL_FROM", "MAIL_TO"), zkUrl=Some("localhost"))           // load from phoenix
val rawEdges = rdd.map{ e => (e("MAIL_FROM").asInstanceOf[VertexId], e("MAIL_TO").asInstanceOf[VertexId]) }   // map to vertexids
val graph = Graph.fromEdgeTuples(rawEdges, 1.0)                                                               // create a graph
val pr = graph.pageRank(0.001)                                                                                // run pagerank
pr.vertices.saveToPhoenix("EMAIL_ENRON_PAGERANK", Seq("ID", "RANK"), zkUrl = Some("localhost"))               // save to phoenix