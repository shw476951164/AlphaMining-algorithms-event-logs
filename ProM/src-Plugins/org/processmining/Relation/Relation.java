package org.processmining.Relation;

public enum Relation {
	DirectFollow,
	DirectCasually,
	Parallel,NoRel,
	reDirectFollow,
	reDirectCasually,IBpJ,JBpI,
	QUad,reQUad,
	reParallel,reNoRel;
	public String toString()//重写toString() 方法
	{
		String symbale = new String();
		symbale = "";
		switch(this)
		{
		   case DirectFollow://直接跟随
			   symbale = " >";
			   break;
		   case DirectCasually://直接因果
			   symbale = "->";
			   break;
		   case Parallel://并行
			   symbale = "||";
			   break;
		   case IBpJ://正三角形关系
			   symbale = "△";
			   break;
		   case QUad://正三角形关系
			   symbale = "□";
			   break;
		   case reDirectFollow://反向直接跟随
			   symbale = " <";
			   break;
		  case reDirectCasually://反向直接因果
			   symbale = "<-";
			   break;
		   case reParallel://反向并行
			   symbale = "||";
			   break;
		   case JBpI://反三角形关系
			   symbale = "△";
			   break;
		   case reQUad://反四边形形关系
			   symbale = "□";
			   break;
			  	  
		   default://无关
			   symbale = " #";
			   break;
		}
		return symbale;
	}
	public Relation getReRelation()
	{
		Relation symbale;
		switch(this)
		{
		   case DirectFollow:
			   symbale = Relation.reDirectFollow;
			   break;
		   case IBpJ:
			   symbale = Relation.JBpI;
			   break;
		   case JBpI:
			   symbale = Relation.IBpJ;
			   break;
		  case DirectCasually:
			   symbale = Relation.reDirectCasually;
			   break;
		   case Parallel:
			   symbale = Relation.reParallel;
			   break;
		   case reDirectFollow:
			   symbale = Relation.DirectFollow;
			   break;
		   case reDirectCasually:
			   symbale = Relation.DirectCasually;
			   break;
		   case reParallel:
			   symbale = Relation.Parallel;
			   break;
		   case QUad:
			   symbale = Relation.reQUad;
			   break;	 
		   case reQUad:
			   symbale = Relation.QUad;
			   break;	   
		   default:
			   symbale = Relation.NoRel;
			   break;
		}
		return symbale;
	}
}
