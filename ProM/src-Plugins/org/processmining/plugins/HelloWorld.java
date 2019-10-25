package org.processmining.plugins;
import java.util.ArrayList;

import org.deckfour.xes.model.XLog;
import org.processmining.Data.MyEvent;
import org.processmining.Data.MyLog;
import org.processmining.FootMatrix.FootMatrix;
import org.processmining.Gather.Triad;
import org.processmining.Gather.Tuple;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetImpl;

public class HelloWorld {
        @Plugin(
                name = "AlphaMining", 
                parameterLabels = {"Xlog"}, 
                		returnLabels = { "AlphaMiningPetrinet" },
            			returnTypes = { Petrinet.class },
                userAccessible = true
        )
        @UITopiaVariant(
                affiliation = "SDUST", 
                author = "Hongwei Sun", 
                email = "sunhongwei_doctor@yeah.net"
        )
        public Petrinet doMining(PluginContext context,XLog log)
    	{
       
    		String label = "PetriNet";
    		
    		PetrinetImpl pnimpl = new PetrinetImpl(label);
    	
    		MyLog mylog = new MyLog(log);
    		
    	  
    		
    		InitialTransition(mylog, pnimpl);
    	
    		FootMatrix footmatrix = CreateRightFootMatrix(mylog, pnimpl);

          
    		
    		
    		
    		CreatePetrinet(pnimpl, footmatrix);
    
    		return pnimpl;
    		
    		
    	}
        private  void InitialTransition(MyLog mylog,PetrinetImpl pnimpl)
    	{
    		
    		ArrayList<MyEvent> myeventlist = new ArrayList<MyEvent>(mylog.getMyEventSet());
    		
    	
    	
    	
    				for(int i = 0; i < myeventlist.size() ; i ++)
    				{
    					MyEvent myevent = myeventlist.get(i);
    					String name = myevent.getName();
    					pnimpl.addTransition(name);
    				}
    		
    			   	}
    	
	private void CreatePetrinet(PetrinetImpl pnimpl, FootMatrix footmatrix) {
		 ArrayList<Triad> casuallist = new ArrayList<Triad>(footmatrix.getDirectCasualSet());
	   	ArrayList<Triad> bplist = new ArrayList<Triad>(footmatrix.getbpset());
	 	ArrayList<Triad> quadlist = new ArrayList<Triad>(footmatrix.getquadset());
	 	ArrayList<Tuple> quadBacklist = new ArrayList<Tuple>(footmatrix.getquadBackTransition());
	 	
		ArrayList<Place> placelist = new ArrayList<Place>();
		placelist.add(pnimpl.addPlace("Start"));
		pnimpl.addArc(placelist.get(0), footmatrix.getFirstTransition());
		
		
		

		for (int i = 0; i < casuallist.size(); i++) {
			Triad triad = casuallist.get(i);
			Transition firstt = triad.getFirst();
			Transition secondt = triad.getSecond();

			if (!(FootMatrix.QuadrangularBDset.contains(secondt) || FootMatrix.QuadrangularCBset.contains(firstt))) {
				Place place = pnimpl.addPlace("p*" + i);

				
				placelist.add(place);
				
				for (int j = 0; j < bplist.size(); j++) {
					Triad bptriad = bplist.get(j);
					if (firstt.equals(bptriad.getFirst()))
					{
						Transition bpend = bptriad.getSecond();

						
						pnimpl.addArc(place, bpend);
					}
					if (secondt.equals(bptriad.getSecond()))
					{
						Transition start = bptriad.getFirst();//

					

						pnimpl.addArc(start, place);

					}
				}
                pnimpl.addArc(firstt, place);
				pnimpl.addArc(place, secondt);
			}

			if (FootMatrix.QuadrangularBDset.contains(secondt) || FootMatrix.QuadrangularCBset.contains(firstt)) {
				

				for (int m = 0; m < quadlist.size(); m++) {

					Triad quadtriad = quadlist.get(m);
					Transition quadend1 = quadtriad.getFirst();
					Transition quadend2 = quadtriad.getSecond();
					
					
					
					
					if (secondt.equals(quadend1)) {

						if (FootMatrix.QuadrangularBDset.contains(quadend1)) {
							Place place1 = pnimpl.addPlace("p#" + m);
							pnimpl.addArc(quadend1, place1);
							pnimpl.addArc(place1, quadend2);

							Place place2 = pnimpl.addPlace("p#" + m);
							pnimpl.addArc(place2, quadend1);
							pnimpl.addArc(quadend2, place2);
							
							pnimpl.addArc(firstt, place2);
					
							for (int k = 0; k < footmatrix.getquadBackTransition().size(); k++) {
							  
								 Tuple tr1=quadBacklist.get(k); 
								 Transition quadBack1=tr1.getFirst();
								 Transition  quadBack2=tr1.getSecond();
								 
							  if (quadend2.equals(quadBack1)) {
								 
								 pnimpl.addArc(place2,quadBack2 );  
								
							}
						}	
							
							  
						

						}

					}
				


				/*	if (firstt.equals(quadend2)) {

						if (FootMatrix.QuadrangularCBset.contains(quadend2)) {
							

						}

					}*/

				}
						
				

			}
         
			
		}

		placelist.add(pnimpl.addPlace("End"));

		pnimpl.addArc(footmatrix.getLastTransition(), placelist.get(placelist.size() - 1));

	}

	private FootMatrix CreateRightFootMatrix(MyLog mylog, PetrinetImpl pnimpl) {
	
		FootMatrix footmatrix = new FootMatrix(mylog, pnimpl);
		footmatrix.ShowMatrix();
		return footmatrix;
	}
}