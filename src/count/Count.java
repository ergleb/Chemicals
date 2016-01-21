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

    final double eps = 1;
    public Count(double p, double k, double c, double q, double alpha) {
        P = p;
        K = k;
        C = c;
        this.q = q;
        this.alpha = alpha;
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


    public double targetf (double[] n, double[] t){//подсчет целефой ф-ии
        double ans = C * n[0] + 2 * n[0] * q * (t[0] * t[1] + t[0] * t[2] + t[1] * t[2]);
        ans -= q * (n[1] * t[0] * t[1] + n[2] * t[0] * t[2] + n[3] * t[1] * t[2]);
        return ans;
    }

    public double alphaf (double[] n, double[] t, double p){//подсчет штрафной ф-ии
        double ans = Math.pow(Math.max(0,-(n[0]*t[0]*t[1]*t[2]-P)),p);
        ans += Math.pow(Math.max(0,-(K - (n[1] * t[0] * t[1] + n[2] * t[0] * t[2] + n[3] * t[1] * t[2]))),p);
        ans += Math.pow(Math.max(0,-t[0]),p) + Math.pow(Math.max(0,-t[1]),p) + Math.pow(Math.max(0,-t[2]),p);
        return ans;
    }

    public double f(double[] n, double[] t, double r, double p){//подсчет общей функции
        return targetf(n, t) + r * alphaf(n, t, p);
    }

    public double[] gradf (double[] n, double[] t, double r, double p){//подсчет градиента
        double[] ans = new double[3];
        double h = 0.001;
        for (int i = 0; i < 3; i++) {
            double[] tShift = Arrays.copyOf(t, 3);
            tShift[i] += h;
            ans[i] = (f(n,tShift,r,p) - f(n,t,r,p))/h;
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

    public double[] gradDescent (double[] n, double[] t, double r, double p, double step){//градиентный спуск(минимизация функций)
        int itn = 0;
        double[] tNew = Arrays.copyOf(t,t.length);
        double[] grad;
        do{
            grad = gradf(n, tNew, r, p);
            itn++;
            for (int i = 0; i < tNew.length; i++) {
                tNew[i] -= step * grad[i];
            }
        } while (norm(grad) > 1 && itn < 10000);
        //System.out.println(itn);
        //System.out.println(Arrays.toString(tNew));
        return tNew;
    }

    public double[] solve (double[] n, double r1, double p, double beta){//метод штрафных функций
        double[] t = new double[3];
        t[1] = t[2] = t[0] = 1;
        int count = 0;
        while (count < 50){
            count++;
            t = gradDescent(n,t,r1,p,0.000001);//тут 0.000001 - шаг градиентного спуска
            double d = r1 * alphaf(n,t,p);
            if (d<eps) return t;
            r1 *= beta;
        }
        return null;
    }

    public static void main(String[] args) {
        Count count = new Count(1000,20,10,3,1.0);//создаем объект с параметрами
        //count.getLucky();//тут превращаем функцию в оптимиста/пессимиста, если нужно
        double[] minn = {1,0,0,0};//выбираем начальное минимальное значение
        double[] mint = count.solve(minn, 10, 1, 2);
        double minf = count.targetf(minn, mint);
        outer: for (int i = 1; i <= 3; i++) {//циклы для перебора целых чисел, тут можно поменять макс. количество контейнеров
            for (int j = 0; j <= i; j++) {
                for (int k = 0; k <= j; k++) {
                    for (int l = 0; l <= k; l++) {
                        double[] n = {i,j,k,l};
                        double[] t = count.solve(n, 10, 1, 2);
                        if(t != null){//если ответ существует, то проверяем минимальность
                            double f = count.targetf(n, t);
                            if (f < minf){
                                minf = f;
                                minn = n;
                                mint = t;
                            }
                        } else continue outer;
                    }
                }
            }
        }
        System.out.println("Number of boxes: " + minn[0]);//выводим на экран
        System.out.println("Size of boxes: " + Arrays.toString(mint));
        System.out.println("Cost: " + minf);


    }
}

