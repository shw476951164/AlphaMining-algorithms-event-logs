package org.processmining.Relation;

public enum Relation {
	DirectFollow,
	DirectCasually,
	Parallel,NoRel,
	reDirectFollow,
	reDirectCasually,IBpJ,JBpI,
	QUad,reQUad,
	reParallel,reNoRel;
	public String toString()//��дtoString() ����
	{
		String symbale = new String();
		symbale = "";
		switch(this)
		{
		   case DirectFollow://ֱ�Ӹ���
			   symbale = " >";
			   break;
		   case DirectCasually://ֱ�����
			   symbale = "->";
			   break;
		   case Parallel://����
			   symbale = "||";
			   break;
		   case IBpJ://�������ι�ϵ
			   symbale = "��";
			   break;
		   case QUad://�������ι�ϵ
			   symbale = "��";
			   break;
		   case reDirectFollow://����ֱ�Ӹ���
			   symbale = " <";
			   break;
		  case reDirectCasually://����ֱ�����
			   symbale = "<-";
			   break;
		   case reParallel://������
			   symbale = "||";
			   break;
		   case JBpI://�������ι�ϵ
			   symbale = "��";
			   break;
		   case reQUad://���ı����ι�ϵ
			   symbale = "��";
			   break;
			  	  
		   default://�޹�
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
