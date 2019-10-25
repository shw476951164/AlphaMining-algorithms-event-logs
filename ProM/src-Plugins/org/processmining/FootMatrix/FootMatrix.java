package org.processmining.FootMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.processmining.Data.MyEvent;
import org.processmining.Data.MyLog;
import org.processmining.Data.MyTrace;
import org.processmining.Gather.Triad;
import org.processmining.Gather.Tuple;
import org.processmining.Relation.Relation;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetImpl;

public class FootMatrix {//足迹矩阵
	private Map<String, Transition> StoTmap;
	private Map<Integer, Transition> ItoTmap;
	private Map<Transition, Integer> TtoImap;
	private MyLog mylog;
	private PetrinetImpl pnimpl;
	private Set<Triad> allrelationset;
	private Set<Triad> directfollowset;
	Transition g, h, c, f;
	private Set<Triad> directcasualset;
	public Set<Triad> backpacksset;
	private Set<Triad> norelset;
	private Set<Triad> bpset;
	private Set<Triad> quadset;
	private Set<Triad> parallelset;
	public Set<Tuple> dfset = new HashSet<Tuple>();
	public Set<String> allelementset = new HashSet<String>();
	public Set<Transition> allset = new HashSet<Transition>();
	public Set<Transition> Callbackset = new HashSet<Transition>();
	public Set<Transition> Callbackset1 = new HashSet<Transition>();
	public Set<Transition> Callbackset2 = new HashSet<Transition>();
	public static Set<Transition> TriangleBDset = new HashSet<Transition>();	
	public static Set<Transition> TriangleCBset = new HashSet<Transition>();	
	public static Set<Transition> QuadrangularBDset = new HashSet<Transition>();	
	public static Set<Transition> QuadrangularCBset = new HashSet<Transition>();	
	public static Set<Transition> endbptuple = new HashSet<Transition>();
	 

	public Set<Tuple> alltupleset = new HashSet<Tuple>();
	private Relation[][] MatrixofRelation;
	private String[][] MatrixtoString;

	public FootMatrix(MyLog log, PetrinetImpl pn)
	//足迹矩阵构造方法

	{
		mylog = log;
		pnimpl = pn;
		StoTmap = new HashMap<String, Transition>();
		TtoImap = new HashMap<Transition, Integer>();
		ItoTmap = new HashMap<Integer, Transition>();
		setMap();
		initial();
		createBaseRelation();
		createRelation();
		createMatrix();
	}

	//成员方法
	private void setMap()

	{
		ArrayList<Transition> translist = new ArrayList<Transition>(pnimpl.getTransitions());

		System.out.println("变迁集合" + translist);
		for (int i = 0; i < translist.size(); i++) {
			Transition t = translist.get(i);
			System.out.println("变迁" + t);

			StoTmap.put(t.toString(), t); 
			System.out.println("StoTmap" + StoTmap);
			TtoImap.put(t, i);
			System.out.println("TtoImap" + TtoImap);
			ItoTmap.put(i, t);
			System.out.println("ItoTmap" + ItoTmap);
		}
		System.out.println("StoTmap大小" + StoTmap.size());
		
	}

	private void initial() 
	{
		allrelationset = new HashSet<Triad>();
		directfollowset = new HashSet<Triad>();
		directcasualset = new HashSet<Triad>();
		backpacksset = new HashSet<Triad>();//
		parallelset = new HashSet<Triad>();
		norelset = new HashSet<Triad>();
		bpset = new HashSet<Triad>();
		quadset = new HashSet<Triad>();
		int size = StoTmap.size();
		MatrixofRelation = new Relation[size][size];
		MatrixtoString = new String[size + 1][size + 1];
		for (int i = 0; i < MatrixofRelation.length; i++)
		{
			for (int j = 0; j < MatrixofRelation.length; j++) {
				MatrixofRelation[i][j] = Relation.NoRel;
			
			}

		}
		
		
		for (int i = 0; i < MatrixofRelation.length; i++) {
			for (int j = 0; j < MatrixofRelation.length; j++) {
				System.out.print(MatrixofRelation[i][j] + " ");
			}
			System.out.println();
		}
		MatrixtoString[0][0] = " ";
		
		for (int i = 0; i < MatrixtoString.length; i++) {
			for (int j = 0; j < MatrixtoString.length; j++) {
				System.out.print(MatrixtoString[i][j] + " ");
			}
			System.out.println();
		}

		

	}

	private void createBaseRelation()
	{
		
		
		for (int i = 0; i < mylog.size(); i++)

		{
			MyTrace trace = mylog.get(i);
			for (int j = 0; j < trace.size() - 1; j++) {
				MyEvent event = trace.get(j);
				String eventstr = event.getName();
				Transition transition = StoTmap.get(eventstr);

				MyEvent event2 = trace.get(j + 1);
				String eventstr2 = event2.getName();
				Transition transition2 = StoTmap.get(eventstr2);

				Tuple tuple = new Tuple(transition, transition2);
				dfset.add(tuple); 
			}
		

		}
		

		Iterator<Tuple> dirit = dfset.iterator();//创建类型为Tuple的迭代器
		while (dirit.hasNext())
		{
			Tuple t = (Tuple) dirit.next();
			Triad triad = new Triad(t, Relation.DirectFollow);
			directfollowset.add(triad);
		}
			}

	private void createRelation()
	
	{
		
		Set<Tuple> alltuple = new HashSet<Tuple>();
		Set<Tuple> dirtuple = new HashSet<Tuple>();

		ArrayList<Triad> dirlist = new ArrayList<Triad>(directfollowset);
		for (int i = 0; i < dirlist.size(); i++) {
			dirtuple.add(dirlist.get(i).getTuple());
			System.out.println("直接元组:" + dirtuple);
		}

	
		alltuple.addAll(dfset);
		
	
		Set<Tuple> parttuple = new HashSet<Tuple>();
		Set<Tuple> paralleltuple = new HashSet<Tuple>();

		Set<Transition> bptuple = new HashSet<Transition>();
		Set<Transition> bptuple1 = new HashSet<Transition>();
		Set<Transition> bptuple2 = new HashSet<Transition>();
	

		Transition eventsp = null;
		
		System.out.println("event=" + eventsp);
		Transition eventend = null;
		System.out.println("event=" + eventend);
		Tuple tuple = null;
		Tuple retuple = null;
		ArrayList<Tuple> alltuplelist = new ArrayList<Tuple>(alltuple);
		
		ArrayList<Tuple> alltuplelist1 = new ArrayList<Tuple>(alltuple);
		
		ArrayList<Tuple> alltuplelist2 = new ArrayList<Tuple>(alltuple);
		
		while (alltuplelist.size() > 0) {

			tuple = alltuplelist.remove(0);
			

			retuple = new Tuple(tuple.getSecond(), tuple.getFirst());

		

			if (alltuplelist.contains(retuple)) {
				allelementset.add(tuple.getFirst().toString());
				allelementset.add(tuple.getSecond().toString());

				alltuplelist.remove(retuple);
				paralleltuple.add(tuple);
				
				
				
			} else {
				parttuple.add(tuple);
				
			}
		}
	
		Iterator<Tuple> parall = paralleltuple.iterator();
		while (parall.hasNext())
		{
			Tuple t1 = (Tuple) parall.next();
			Triad triad1 = new Triad(t1, Relation.Parallel);
			parallelset.add(triad1);
		}
		

		
		
		Set<Transition> eventspset = new HashSet<Transition>();//用于存放并发初始变迁集
		Set<Transition> eventendset = new HashSet<Transition>();//用于存放并发结束变迁集
		
		
		while (alltuplelist1.size() > 0) {
			tuple = alltuplelist1.remove(0);
			if (allelementset.contains(tuple.getSecond().toString())) {
				if (allelementset.contains(tuple.getFirst().toString())) {
					
				} else {
					eventsp = tuple.getFirst();
					eventspset.add(eventsp);
					
					

				}
			}
			if (allelementset.contains(tuple.getFirst().toString())) {
				if (allelementset.contains(tuple.getSecond().toString())) {
					System.out.println("eventend" + tuple.getSecond().toString());
				} else {
					eventend = tuple.getSecond();
					eventendset.add(eventend);
					
				}
			}
		}
	
	
		
		
		Transition[] eventsparr = new Transition[	eventspset.size()];//并发前初始变迁数组
         eventspset.toArray(eventsparr);
		
        Transition[] eventendarr = new Transition[eventendset.size()];//并发后结束变迁数组
         eventendset.toArray(eventendarr);
		
		
		while (alltuplelist2.size() > 0) {
			tuple = alltuplelist2.remove(0);

			for (int i = 0; i < eventspset.size(); i++) {
				
				if (tuple.getFirst().toString().equals(eventsparr[i].toString())) {
					bptuple1.add(tuple.getSecond());
					bptuple.add(tuple.getSecond());
					bptuple2.add(tuple.getSecond());
					
				}
			}
			for (int i = 0; i < eventendarr.length; i++) {
				if(tuple.getSecond().toString().equals(eventendarr[i].toString()))
				{
						endbptuple.add(tuple.getFirst());
						
					}

			}

				
			
		}
		
		
	
		Transition[] bptuplearr = new Transition[bptuple1.size()];
		bptuple1.toArray(bptuplearr);	
		
		
		for (int i = 0; i < bptuplearr.length; i++) {
			if (endbptuple.contains(bptuplearr[i])) {

				endbptuple.remove(bptuplearr[i]);

			}

		}	
		
	
		String[] arr = new String[allelementset.size()];
		allelementset.toArray(arr);

		int[] logtotal0 = new int[arr.length];
		int[][] tracetotal0 = new int[mylog.size()][arr.length];
		int[][] tracetotal01 = new int[mylog.size()][arr.length];
		
		
	
				for (int i = 0; i < mylog.size(); i++) {
					MyTrace testtracetotal = mylog.get(i);//迹

					for (int j = 0; j < testtracetotal.size(); j++) {
						MyEvent testeventtotal = testtracetotal.get(j);//事件

						for (int k = 0; k < bptuplearr.length; k++) {
						if (testeventtotal.toString().equals(bptuplearr[k].toString())) {
								tracetotal0[i][k] = tracetotal0[i][k] + 1;//每条迹并行元素的总数
                             
							}
                          if (tracetotal0[i][k]>1) {
                        	  
                        	bptuple1.remove(bptuplearr[k]);
                        	
						}
						}

					}
				}
		
				
				
				Transition[] bptuplearr1 = new Transition[bptuple1.size()];	
				bptuple1.toArray(bptuplearr1);
				
	     	for (int i = 0; i < bptuplearr1.length; i++) {
			
	     		bptuple.remove(bptuplearr1[i]);
	     	
	    	}
		
		
		
		

		Set<Tuple> dircasualtuple = new HashSet<Tuple>(dirtuple);
		dircasualtuple.retainAll(parttuple);
		

		ArrayList<Tuple> parallellist = new ArrayList<Tuple>(paralleltuple);
		ArrayList<Tuple> parallellist1 = new ArrayList<Tuple>(paralleltuple);
		
		ArrayList<Tuple> dircasuallist = new ArrayList<Tuple>(dircasualtuple);

		
		for (int i = 0; i < parallellist.size(); i++) {
			Tuple t = parallellist.get(i);
			
			parallelset.add(new Triad(t, Relation.Parallel));
			

		}
		
		for (int i = 0; i < dircasuallist.size(); i++) {
			Tuple t = dircasuallist.get(i);
			directcasualset.add(new Triad(t, Relation.DirectCasually));
			
		}

		for (int i = 0; i < ItoTmap.size(); i++) {
			Transition tra = ItoTmap.get(i);
			

			Tuple tup = new Tuple(tra, tra);

			norelset.add(new Triad(tup, Relation.NoRel));
			
		}

		allrelationset.addAll(directcasualset);
		
		allrelationset.addAll(parallelset);
		allrelationset.addAll(norelset);

		
		Iterator<String> iter = allelementset.iterator();
		Iterator<String> iter2 = allelementset.iterator();
		while (iter.hasNext()) {
			String ttem = iter.next();

			if (bptuple2.contains(StoTmap.get(ttem)))
			{
				continue;
			} else {
				
				
		       Callbackset1.add(StoTmap.get(ttem));
		   	   Callbackset.add(StoTmap.get(ttem));
		   	   
				
			}
		}

	
		
		

		Transition[] Callbackset1arr = new Transition[Callbackset1.size()];
		Callbackset1.toArray(Callbackset1arr);
		
		for (int i = 0; i < mylog.size(); i++) {
			MyTrace testtracetotal = mylog.get(i);

			for (int j = 0; j < testtracetotal.size(); j++) {
				MyEvent testeventtotal = testtracetotal.get(j);

				for (int k = 0; k < Callbackset1arr.length; k++) {
				if (testeventtotal.toString().equals(Callbackset1arr[k].toString())) {
						tracetotal01[i][k] = tracetotal01[i][k] + 1;
                     
					}
                  if (tracetotal01[i][k]>1) {
                	  
                	  Callbackset1.remove(Callbackset1arr[k]);
                					}
				}

			}
		}
		
		Transition[] endbptuplearr1 = new Transition[Callbackset1.size()];	
		Callbackset1.toArray(endbptuplearr1);
		
 	for (int i = 0; i < endbptuplearr1.length; i++) {
	
 		 Callbackset.remove(endbptuplearr1[i]);
 	
	}
 
 	
	
 	
 	
 	
 	
 	
 	
 	
		while (iter2.hasNext()) {
			allset.add(StoTmap.get(iter2.next()));
		}

		

		Transition[] iterarr3 = new Transition[bptuple.size()];
		Transition[] iterarr4 = new Transition[Callbackset.size()];

		bptuple.toArray(iterarr3);
		Callbackset.toArray(iterarr4);
		

	
		
		while (dircasuallist.size() > 0) {
			Tuple tuple1 = dircasuallist.remove(0);
			
			for (int i = 0; i <eventendarr.length; i++) {
				if (tuple1.getSecond().toString().equals(eventendarr[i].toString())) {

	
					if (bptuple.toString().contains(tuple1.getFirst().toString())) {
						TriangleBDset.add(tuple1.getFirst());

					}
					if (Callbackset.toString().contains(tuple1.getFirst().toString())) {
						QuadrangularCBset.add(tuple1.getFirst());

					}

				}	
			
			}
			
		
		}

		

		Transition[] iterarr5 = new Transition[TriangleBDset.size()];
		Transition[] iterarr6 = new Transition[QuadrangularCBset.size()];

		

		TriangleBDset.toArray(iterarr5);
		QuadrangularCBset.toArray(iterarr6);

		

		List BDlist1 = Arrays.asList(iterarr3);
		List BDarrList1 = new ArrayList(BDlist1);
		List CBlist2 = Arrays.asList(iterarr4);
		List CBarrList2 = new ArrayList(CBlist2);

		for (int i = 0; i < iterarr5.length; i++) {

			for (int j = 0; j < iterarr3.length; j++) {

				if (iterarr5[i].toString().contains(iterarr3[j].toString())) {

					BDarrList1.remove(iterarr3[j]);
					
					break;

				}
				
			}

		}

		Transition[] iterarr9 = new Transition[BDarrList1.size()];
		BDarrList1.toArray(iterarr9);

		for (int i = 0; i < iterarr3.length - iterarr5.length; i++) {

			QuadrangularBDset.add(iterarr9[i]);

		}

		

		for (int i = 0; i < iterarr6.length; i++) {
			for (int j = 0; j < iterarr4.length; j++) {

				if (iterarr6[i].equals(iterarr4[j])) {

					CBarrList2.remove(iterarr4[j]);

					
					break;

				}
			
			}
		}
		Transition[] iterarr10 = new Transition[CBarrList2.size()];
		CBarrList2.toArray(iterarr10);

		for (int i = 0; i < iterarr4.length - iterarr6.length; i++) {

			TriangleCBset.add(iterarr10[i]);

		}

		Transition[] iterarr7 = new Transition[TriangleCBset.size()]; 
		Transition[] iterarr8 = new Transition[QuadrangularBDset.size()]; 

		TriangleCBset.toArray(iterarr7);
		QuadrangularBDset.toArray(iterarr8);  

		

		
		
		for (int i = 0; i < iterarr5.length; i++) {
			
			for (int j = 0; j < iterarr7.length; j++) {
				
				
				Tuple temtup = new Tuple(iterarr5[i], iterarr7[j]);
				Tuple temtuptransfer = new Tuple(iterarr7[j], iterarr5[i]);
				if ( parallellist1.contains(temtup)||parallellist1.contains(temtuptransfer)) {
					bpset.add(new Triad(temtup, Relation.JBpI));
					bpset.add(new Triad(temtuptransfer, Relation.JBpI));
				}
				
				
				
			}
		}
		
		
		
		
		for (int i = 0; i < iterarr8.length; i++) {

			for (int j = 0; j < iterarr6.length; j++) {
				
			
				Tuple temtup = new Tuple(iterarr8[i], iterarr6[j]);
				Tuple temtuptransfer = new Tuple(iterarr6[j], iterarr8[i]);	
	
				if (parallellist1.contains(temtup)||parallellist1.contains(temtuptransfer)) {
					quadset.add(new Triad(temtup, Relation.QUad));
					
					quadset.add(new Triad(temtuptransfer, Relation.QUad));
					
		}		
							
			}
		}

	

		
		
		int[] index = new int[bptuple.size()];
		int l = 0;
		List indexlist = new ArrayList();

		for (int i = 0; i < arr.length; i++) {
			if (bptuple.toString().contains(arr[i])) {
				index[l++] = i;
			}
		}
		for (int i = 0; i < index.length; i++) {
			System.out.println(index[i]);
			indexlist.add(index[i]);
		}
		
		

		int[] logtotal = new int[arr.length];
		int[][] tracetotal = new int[mylog.size()][arr.length];

		
		for (int i = 0; i < arr.length; i++) {
			System.out.println("arr" + arr[i]);
		}
		for (int i = 0; i < arr.length; i++)
		{
			logtotal[i] = 0;
			System.out.print(logtotal[i] + " ");

		}

		System.out.println();

		for (int i = 0; i < mylog.size(); i++)
		{
			for (int j = 0; j < arr.length; j++) {
				tracetotal[i][j] = 0;

				System.out.print(tracetotal[i][j] + " ");

			}
			System.out.println();
		}

		
		for (int i = 0; i < mylog.size(); i++) {
			MyTrace testtracetotal = mylog.get(i);

			for (int j = 0; j < testtracetotal.size(); j++) {
				MyEvent testeventtotal = testtracetotal.get(j);

				for (int k = 0; k < arr.length; k++) {
					if (testeventtotal.toString().equals(arr[k].toString())) {
						tracetotal[i][k] = tracetotal[i][k] + 1;

					}

				}

			}
		}
		

		for (int i = 0; i < mylog.size(); i++)
		{
			for (int j = 0; j < arr.length; j++) {

				System.out.print(tracetotal[i][j] + " ");

			}
			System.out.println();
		}

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < mylog.size(); j++) {
				logtotal[i] += tracetotal[j][i];
			}
		}

		for (int i = 0; i < arr.length; i++) {
			
			System.out.printf("总数 %d", logtotal[i]);
		}

		
		for (int i = 0; i < mylog.size(); i++) {
			for (int j = 0; j < arr.length; j++) {
				System.out.printf("%d ", tracetotal[i][j]);
			}
			System.out.println("\n");
		}

	
         int fg1 = 0, fg2 = 0;

		ao: for (int i = 0; i < arr.length; i++) {
			int fg0 = logtotal[i] - mylog.size();//logtotal[]表示并行集合元素的数目
			for (int j = 0; j < arr.length; j++) {
				if (fg0 == logtotal[j]) {
					fg1 = 1;
					i = arr.length + 3;
					break ao;
				}
			}
		}
		System.out.println("fg1" + fg1);
		
		ko:
		for (int i = 0; i < mylog.size(); i++) {
			for (int j = 0; j < arr.length; j++) {
				for (int k = 0; k < arr.length; k++) {
					if (tracetotal[i][j] - tracetotal[i][k] == 1)//说明j和k 列的元素在三角形循环上
					{
						fg2 = 1;
						break ko;
					}

				}
			}
		}
		
		
		System.out.println("fg2" + fg2);

		
		if (fg1 == 1 && fg2 == 1)
		{

			System.out.println(Arrays.toString(arr));
			
			int[][] locationmx = new int[mylog.size()][arr.length];
			
			for (int i = 0; i < mylog.size(); i++) {
				for (int j = 0; j < arr.length; j++) {
					locationmx[i][j] = 0;
					System.out.print(locationmx[i][j] + " ");
				}
				System.out.println();
			}
			
			for (int i = 0; i < mylog.size(); i++) {
				MyTrace trace1 = mylog.get(i);
				for (int j = 0; j < trace1.size(); j++)
				{
					MyEvent event1 = trace1.get(j);
					for (int k = 0; k < arr.length; k++) {
						if (event1.toString().equals(arr[k]) && locationmx[i][k] == 0) {
							locationmx[i][k] = j;
						}
					}
				}
			}
			 
			for (int i = 0; i < mylog.size(); i++)

			{
				for (int j = 0; j < arr.length; j++) {
					System.out.printf("%d ", locationmx[i][j]);
				}
				System.out.println("\n");
			}

			
			for (int k = 0; k < index.length; k++)
				for (int i = 0; i < mylog.size(); i++) {
					for (int j = 0; j < arr.length; j++) {
						if (locationmx[i][j] < locationmx[i][index[k]] && locationmx[i][j] != 0
								&& indexlist.contains(j) == false) {
							
							Transition transition1 = StoTmap.get(arr[j]);
							Transition transition2 = StoTmap.get(arr[index[k]]);
							Tuple temtup = new Tuple(transition1, transition2);
							Tuple temtuptransfer = new Tuple(transition2, transition1);
							bpset.remove(new Triad(temtup, Relation.JBpI));
							bpset.remove(new Triad(temtuptransfer, Relation.JBpI));			
						}

					}
				}

		}
	
		   int fg3 = 0, fg4 = 0;

		
			ko1: 
			for (int i = 0; i < mylog.size(); i++) {
				for (int j = 0; j < arr.length; j++) {
					for (int k = 0; k < arr.length; k++) {
						if (tracetotal[i][j] ==tracetotal[i][k] )
						{
							fg3 = 1;
							break ko1;
						}

					}
				}
			}
			
			
			System.out.println("fg3" + fg3);

		if (fg3==1) {
			
			System.out.println(Arrays.toString(arr));
			
			int[][] locationmx = new int[mylog.size()][arr.length];
		
			for (int i = 0; i < mylog.size(); i++) {
				for (int j = 0; j < arr.length; j++) {
					locationmx[i][j] = 0;
					System.out.print(locationmx[i][j] + " ");
				}
				System.out.println();
			}
			
			for (int i = 0; i < mylog.size(); i++) {
				MyTrace trace1 = mylog.get(i);
				for (int j = 0; j < trace1.size(); j++)
				{
					MyEvent event1 = trace1.get(j);
					for (int k = 0; k < arr.length; k++) {
						if (event1.toString().equals(arr[k]) && locationmx[i][k] == 0) {
							locationmx[i][k] = j;
						}
					}
				}
			}
			
			
			
			for (int k = 0; k < index.length; k++)
				for (int i = 0; i < mylog.size(); i++) {
					for (int j = 0; j < arr.length; j++) {
						if (locationmx[i][j] < locationmx[i][index[k]]
								&& indexlist.contains(j) == false) {
							//Transition transition = StoTmap.get(eventstr);
							Transition transition1 = StoTmap.get(arr[j]);
							Transition transition2 = StoTmap.get(arr[index[k]]);
							Tuple temtup = new Tuple(transition1, transition2);
							Tuple temtuptransfer = new Tuple(transition2, transition1);
							quadset.remove(new Triad(temtup, Relation.QUad));
							quadset.remove(new Triad(temtuptransfer, Relation.QUad));			
						}

					}
				}
		
		}
	
		allrelationset.addAll(bpset);
		allrelationset.addAll(quadset);

	}

	
	
	
	private Tuple Tuple(String flag, int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	private void createMatrix() {
		ArrayList<Triad> alltriadlist = new ArrayList<Triad>(allrelationset);
		
		for (int i = 0; i < alltriadlist.size(); i++) {
			Triad triad = alltriadlist.get(i);
			Transition first = triad.getFirst();
			Transition second = triad.getSecond();
			Relation relation = triad.getRelation();
			int f = TtoImap.get(first);
			int s = TtoImap.get(second);
			MatrixofRelation[f][s] = relation;
			MatrixofRelation[s][f] = relation.getReRelation();
		}
	}

	public Set<Relation> getRowRelation(Transition t) {
		Set<Relation> set = new HashSet<Relation>();
		int len = TtoImap.get(t);
		for (int i = 0; i < MatrixofRelation.length; i++) {
			set.add(MatrixofRelation[len][i]);
		}
		return set;
	}

	public Set<Relation> getColumnRelation(Transition t) {
		Set<Relation> set = new HashSet<Relation>();
		int len = TtoImap.get(t);
		for (int i = 0; i < MatrixofRelation.length; i++) {
			set.add(MatrixofRelation[i][len]);
		}
		return set;
	}

	public void changeInCasualRelation(Set<Triad> s) {
		ArrayList<Triad> tl = new ArrayList<Triad>(s);
		for (int i = 0; i < tl.size(); i++) {
			Triad triad = tl.get(i);
			System.out.println(triad);
			Transition t1 = triad.getFirst();
			Transition t2 = triad.getSecond();
			int t1pos = TtoImap.get(t1);
			int t2pos = TtoImap.get(t2);
			changeRelation(t1pos, t2pos, Relation.DirectCasually);
		}
		reSetRelation();
	}

	private void changeRelation(int i, int j, Relation r) {
		MatrixofRelation[i][j] = r;
		MatrixofRelation[j][i] = r.getReRelation();
	}

	private void reSetRelation() {
		directcasualset.clear();
		directcasualset.addAll(getNewRelation(Relation.DirectCasually));
		//indirectcasualset.clear();
		//indirectcasualset.addAll(getNewRelation(Relation.InDirectCasually));

		norelset.clear();
		norelset.addAll(getNewRelation(Relation.NoRel));
	}

	private Set<Triad> getNewRelation(Relation r) {
		Set<Triad> set = new HashSet<Triad>();
		for (int i = 0; i < MatrixofRelation.length; i++) {
			for (int j = 0; j < MatrixofRelation.length; j++) {
				Relation rel = MatrixofRelation[i][j];
				if (rel.equals(r)) {
					Transition t1 = ItoTmap.get(i);
					Transition t2 = ItoTmap.get(j);
					set.add(new Triad(new Tuple(t1, t2), r));
				}
			}
		}
		return set;
	}

	public Set<Triad> getDirectFollowSet() {

		return directfollowset;
	}

	public Set<Triad> getbpset() {
		return bpset;
	}

	public Set<Triad> getquadset() {

		return quadset;
	}

	public Set<Triad> getDirectCasualSet() {
		return directcasualset;
	}

	public Set<Triad> getNoRelSet() {
		return norelset;
	}

	public Map<String, Transition> getStoTmap() {
		return StoTmap;
	}

	public Map<Transition, Integer> getTtoImap() {
		return TtoImap;
	}

	public Map<Integer, Transition> getItoTmap() {
		return ItoTmap;
	}

	public Relation[][] getMatrixofRelation() {
		return MatrixofRelation;
	}

	public void ShowMatrix() {
		for (int i = 1; i < MatrixtoString.length; i++) {
			MatrixtoString[0][i] = ItoTmap.get(i - 1).toString();
			MatrixtoString[i][0] = ItoTmap.get(i - 1).toString();
		}
		for (int i = 0; i < MatrixofRelation.length; i++) {
			for (int j = 0; j < MatrixofRelation.length; j++) {
				MatrixtoString[i + 1][j + 1] = MatrixofRelation[i][j].toString();
			}
		}
		for (int i = 0; i < MatrixtoString.length; i++)
		{
			for (int j = 0; j < MatrixtoString.length; j++) {
				System.out.printf("%4s", MatrixtoString[i][j]);
			}
			System.out.println();
		}
	}

	public Transition getFirstTransition() {
		Transition t = StoTmap.get(mylog.getFirstTrace().getFirstEvent().getName());
		return t;

	}

	public Transition getLastTransition() {
		Transition t = StoTmap.get(mylog.getFirstTrace().getLastEvent().getName());
		return t;
	}

	public Set<Tuple> getquadBackTransition() {
		Transition t = null;
		Transition t1 = null;
		Set<Tuple> quadquadBackset  = new HashSet<Tuple>();
		
		Transition[] quadquadBackarr = new Transition[quadquadBackset.size()]; 
		
		
		ArrayList<Triad> casuallist1 = new ArrayList<Triad>(getDirectCasualSet());
		
		
		for (int m = 0; m < casuallist1.size(); m++) {

			Triad triad = casuallist1.get(m);
			Transition first1 = triad.getFirst();
			Transition second1 = triad.getSecond();
			if (QuadrangularBDset.contains(second1) || FootMatrix.QuadrangularCBset.contains(first1)) {
				ArrayList<Triad> quadlist1 = new ArrayList<Triad>(getquadset());
				for (int i = 0; i < quadlist1.size(); i++) {
					Triad quadtriad1 = quadlist1.get(i);
					Transition quadfirst2 = quadtriad1.getFirst();

					Transition quadsecond2 = quadtriad1.getSecond();

					if (first1 == quadsecond2) {
						t = second1;
						t1=first1;
					
				Tuple tuple1=new Tuple(t1,t);
					quadquadBackset.add(tuple1);
				
						
						
					System.out.println("t的值为" + t);
					}
				}
			}
		}
		
		return quadquadBackset;

	}

	public Transition getquadPreviousTransition() {
		Transition t = null;
		ArrayList<Triad> casuallist1 = new ArrayList<Triad>(getDirectCasualSet());
		ArrayList<Triad> quadlist1 = new ArrayList<Triad>(getquadset());
		for (int m = 0; m < casuallist1.size(); m++) {

			Triad triad = casuallist1.get(m);
			Transition first1 = triad.getFirst();
			Transition second1 = triad.getSecond();
			for (int i = 0; i < quadlist1.size(); i++) {
				Triad quadtriad1 = quadlist1.get(m);
				Transition quadfirst2 = quadtriad1.getFirst();
				Transition quadsecond2 = quadtriad1.getSecond();

				if (second1 == quadfirst2) {
					t = first1;

				}
			}
		}
		return t;
	}

}
