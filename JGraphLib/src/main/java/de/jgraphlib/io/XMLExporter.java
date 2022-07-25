package de.jgraphlib.io;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.EdgeWeight;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.util.Tuple;

public class XMLExporter<V extends Vertex<P>, P, E extends WeightedEdge<W>, W extends EdgeWeight> {

	WeightedGraph<V, P, E, W> graph;
	VertexPositionMapper<P> vertexPositionMapper;
	//EdgeWeightMapper<W> edgeWeightMapper;

	public XMLExporter(WeightedGraph<V, P, E, W> graph, VertexPositionMapper<P> vertexPositionInterface /*,
			EdgeWeightMapper<W> edgeWeightInterface*/) {
		this.graph = graph;
		this.vertexPositionMapper = vertexPositionInterface;
		//this.edgeWeightMapper = edgeWeightInterface;
	}

	public boolean exportGraph(String xmlFilePath) {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			Element XMLGraph = document.createElement("graph");

			if (graph.isDirected())
				XMLGraph.setAttribute("directed", "yes");
			else
				XMLGraph.setAttribute("directed", "no");

			document.appendChild(XMLGraph);

			// Vertices
			Element XMLVertices = document.createElement("vertices");
			XMLGraph.appendChild(XMLVertices);

			for (V vertex : graph.getVertices())
				XMLVertices.appendChild(getXmlVertex(document, vertex));

			// Edges
			Element XMLEdges = document.createElement("edges");
			XMLGraph.appendChild(XMLEdges);

			for (E edge : graph.getEdges())
				XMLEdges.appendChild(getXmlEdge(document, edge));

			/*Element XMLAdjacencies = document.createElement("adjacencies");
			XMLGraph.appendChild(XMLAdjacencies);			
			 * for (int i = 0; i < graph.getVertexAdjacencies().size(); i++)
			 * XMLAdjacencies.appendChild(getXmlElement(document, XMLAdjacencies,
			 * String.format("vertex%d", i),
			 * graph.getVertexAdjacencies().get(i).stream().map(tuple ->
			 * String.valueOf(tuple.getSecond())) .collect(Collectors.joining(","))));
			 */

			//toConsole(document);
			toXmlFile(document, xmlFilePath);
			return true;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void toXmlFile(Document document, String xmlFilePath) {
		try {
			File xmlFile = new File(xmlFilePath);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(new DOMSource(document), new StreamResult(xmlFile));
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public void toConsole(Document document) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(document);
			StreamResult console = new StreamResult(System.out);
			transformer.transform(source, console);

		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	private Node getXmlVertex(Document document, V vertex) {
		Element XMLVertex = document.createElement("vertex");
		XMLVertex.setAttribute("ID", Integer.toString(vertex.getID()));

		List<Tuple<String, String>> attributesValues = vertexPositionMapper.translate(vertex.getPosition());

		for (Tuple<String, String> attributeValue : attributesValues)
			XMLVertex.appendChild(
					getXmlElement(document, XMLVertex, attributeValue.getFirst(), attributeValue.getSecond()));

		return XMLVertex;
	}

	private Node getXmlEdge(Document document, E edge) {
		Element XMLEdge = document.createElement("edge");
		XMLEdge.setAttribute("ID", Integer.toString(edge.getID()));

		Tuple<V, V> vertices = graph.getVerticesOf(edge);
		XMLEdge.appendChild(getXmlElement(document, XMLEdge, "source", Integer.toString(vertices.getFirst().getID())));
		XMLEdge.appendChild(getXmlElement(document, XMLEdge, "target", Integer.toString(vertices.getSecond().getID())));

		/*Element XMLEdgeWeight = document.createElement("weight");
		XMLEdge.appendChild(XMLEdgeWeight);

		List<Tuple<String, String>> edgeWeightAttributeValuePairs = edgeWeightMapper.translate(edge.getWeight());
		for (Tuple<String, String> edgeWeightAttributeValuePair : edgeWeightAttributeValuePairs)
			XMLEdgeWeight.appendChild(getXmlElement(document, XMLEdgeWeight, edgeWeightAttributeValuePair.getFirst(),
					edgeWeightAttributeValuePair.getSecond()));*/

		return XMLEdge;
	}

	private Node getXmlElement(Document document, Element element, String name, String value) {
		Element node = document.createElement(name);
		node.appendChild(document.createTextNode(value));
		return node;
	}
}
