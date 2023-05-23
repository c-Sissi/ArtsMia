package it.polito.tdp.artsmia.model;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject,DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer,ArtObject> objIdMap;
	
	public Model() {
		dao = new ArtsmiaDAO();
		objIdMap = new HashMap<Integer,ArtObject>();
	}	
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
//		AGGIUNGO I VERTICI
		dao.listObjects(objIdMap);
		Graphs.addAllVertices(this.grafo, objIdMap.values());		
		
//		AGGIUNGO GLI ARCHI
		
//		METODO 1 --> NON GIUNGE A TERMINE
//		for(ArtObject a1: this.grafo.vertexSet()) {
//			for(ArtObject a2: this.grafo.vertexSet()) {
//				if(!a1.equals(a2) && !this.grafo.containsEdge(a1,a2)) {
////					CHIEDO AL DB SE DEVO COLLEGARE I DUE VERTICI
//					int peso = dao.getPeso(a1,a2);
//					if(peso > 0) {
//						Graphs.addEdgeWithVertices(this.grafo, a1, a2);
//					}
//				}
//			}
//		}
		
//		METODO 2:
		for(Adiacenze a: this.dao.getAdiacenze(objIdMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2());
			
		}
	}
	public int numVertici() {
		return this.grafo.vertexSet().size();
			
	}
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
	public ArtObject getObject(int objId) {
		return objIdMap.get(objId);
	}
	public int getComponenteConnessa(ArtObject vertice) {
		Set<ArtObject> visitati = new HashSet<>();
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> it = 
				new DepthFirstIterator<>(this.grafo,vertice);
		while (it.hasNext()) {
			visitati.add(it.next());
		}
		return visitati.size();
	}
	
}
