package count;

import java.util.Arrays;

/**
 * Created by Hlib on 17.01.2016.
 */
public class Count {
    double P;
    double K;
    double C;
    double q;
    double alpha;
    double[] tMax;

    final double eps = 0.1;
    public Count(double p, double k, double c, double q, double alpha) {
        P = p;
        K = k;
        C = c;
        this.q = q;
        this.alpha = alpha;
    }

    public Count(double p, double k, double c, double q, double alpha, double[] tMax) {
        P = p;
        K = k;
        C = c;
        this.q = q;
        this.alpha = alpha;
        this.tMax = tMax;
    }

    public void getLucky(){//превращает задачу в задачу оптимиста
        P = P * (1 - Math.sqrt(1 / alpha - 1));
        C = C * (1 - Math.sqrt(Math.log(Math.pow(alpha,-2))));
        q = q * (1 - Math.sqrt(2 * (1/alpha - 1)));
    }

    public void getUnLucky(){//превращает в задачу пессимиста
        P = P * (1 + Math.sqrt(1 / alpha - 1));
        C = C * (1 + Math.sqrt(Math.log(Math.pow(alpha,-2))));
        q = q * (1 + Math.sqrt(2* (1/alpha - 1)));
    }


    public double targetf (double n, double n0, double[] t){//подсчет целефой ф-ии
        double ans = C * n + 2 * n * q * (t[0] * t[1] + t[0] * t[2] + t[1] * t[2]);
        ans -= q * (n0 * t[0] * t[1] + n0 * t[0] * t[2] + n0 * t[1] * t[2]);
        return ans;
    }

    public double alphaf (double n, double n0, double[] t, double p){//подсчет штрафной ф-ии
        double ans = Math.pow(Math.max(0,-(n*t[0]*t[1]*t[2]-P)),p);
        ans += Math.pow(Math.max(0,-(K - (n0 * t[0] * t[1] + n0 * t[0] * t[2] + n0 * t[1] * t[2]))),p);
        ans += Math.pow(Math.max(0,-t[0]),p) + Math.pow(Math.max(0,-t[1]),p) + Math.pow(Math.max(0,-t[2]),p);
        ans += Math.pow(Math.max(0,t[0]-tMax[0]),p) + Math.pow(Math.max(0,t[1]-tMax[1]),p) + Math.pow(Math.max(0,t[2]-tMax[2]),p);
        return ans;
    }

    public double f(double n, double n0, double[] t, double r, double p){//подсчет общей функции
        return targetf(n, n0, t) + r * alphaf(n, n0, t, p);
    }

    public double[] gradf (double n, double n0, double[] t, double r, double p){//подсчет градиента
        double[] ans = new double[3];
        double h = 0.001;
        for (int i = 0; i < 3; i++) {
            double[] tShift = Arrays.copyOf(t, 3);
            tShift[i] += h;
            ans[i] = (f(n,n0,tShift,r,p) - f(n,n0,t,r,p))/h;
        }
        return ans;
    }

    double norm (double[] x){//подсчет нормы вектора
        double ans = 0;
        for (double el : x){
            ans += Math.pow(el,2);
        }
        return Math.sqrt(ans);
    }

    public double[] gradDescent (double n, double n0, double[] t, double r, double p, double step){//градиентный спуск(минимизация функций)
        int itn = 0;
        double[] tNew = Arrays.copyOf(t,t.length);
        double[] grad;
        do{
            grad = gradf(n, n0, tNew, r, p);
            itn++;
            for (int i = 0; i < tNew.length; i++) {
                tNew[i] -= step * grad[i];
            }
        } while (norm(grad) > 1 && itn < 10000);
        //System.out.println(itn);
        //System.out.println(Arrays.toString(tNew));
        return tNew;
    }

    public double[] solve (double n, double n0, double r1, double p, double beta){//метод штрафных функций
        double[] t = new double[3];
        t[1] = t[2] = t[0] = 1;
        int count = 0;
        while (count < 50){
            count++;
            t = gradDescent(n,n0,t,r1,p,0.0000001);//тут 0.000001 - шаг градиентного спуска
            double d = r1 * alphaf(n,n0,t,p);
            if (d<eps) return t;
            r1 *= beta;
        }
        return null;
    }

    public double getP() {
        return P;
    }

    public static void main(String[] args) {
        Count count = new Count(1000,53,10,3,0.8,new double[]{6,4,3});//создаем объект с параметрами
        //count.getUnLucky();//тут превращаем функцию в оптимиста/пессимиста, если нужно
        double mincont = Math.ceil(count.P/(6*4*3));
        double minn = 0;
        double minn0 = 0;
        double[] mint = null;
        double minf = Double.MAX_VALUE;
        outer: for (int i = (int)mincont; i <= (int)mincont+5; i++) {//циклы для перебора целых чисел, тут можно поменять макс. количество контейнеров
            for (int j = 0; j <= i; j++) {
                double[] t = count.solve(i,j,10,1,2);
                System.out.println(i + " " + j);
                if (t!=null) {
                    System.out.println(Arrays.toString(t));
                    System.out.println(count.targetf(i,j,t));
                        if(count.targetf(i,j,t)<minf){
                            minn = i;
                            minn0 = j;
                            mint = t;
                            minf = count.targetf(i,j,t);
                        }
                    }

                else continue outer;
            }
        }
        try {
            System.out.println("Количество контейнеров: " + minn);//выводим на экран
            System.out.println("Размеры: " + Arrays.toString(mint));
            System.out.println("Стоимость: " + minf);
        }
        catch (NullPointerException ex){
            System.out.println("can't do it");
        }
    }
}

