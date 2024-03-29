package application;

public class calculate {
	// 方差s^2=[(x1-x)^2 +...(xn-x)^2]/n 或者s^2=[(x1-x)^2 +...(xn-x)^2]/(n-1)
	 public static double Variance(double[] x) { 
	        int m=x.length;
	          double sum=0;
	         for(int i=0;i<m;i++){//求和
	             sum+=x[i];
	          }
	        double dAve=sum/m;//求平均值
	          double dVar=0;
	         for(int i=0;i<m;i++){//求方差
	             dVar+=(x[i]-dAve)*(x[i]-dAve);
	         }
	         return dVar/m;
	     }
	     
	     //标准差σ=sqrt(s^2)
	     public static double StandardDiviation(double[] x) { 
	         int m=x.length;
	         double sum=0;
	         for(int i=0;i<m;i++){//求和
	             sum+=x[i];
	         }
	         double dAve=sum/m;//求平均值
	         double dVar=0;
	         for(int i=0;i<m;i++){//求方差
	             dVar+=(x[i]-dAve)*(x[i]-dAve);
	         }
	                 //reture Math.sqrt(dVar/(m-1));
	         return Math.sqrt(dVar/m);    
	     }
}
