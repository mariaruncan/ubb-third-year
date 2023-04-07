%% Aplicații la formula lui Taylor
% *P1*. Să se scrie formula lui MacLaurin pentru funcţia $f:[a,\infty]\rightarrow\mathbb{R}$, 
% $f(x)=\sqrt{a+x}$, $a>0$.

syms a x f expo xi n
assume(a>0)
expo=sym(1)/sym(2);
f=a^expo*(1+x/a)^expo;
n=3;
Tnf=taylor(f,x,0,'Order',n+1)
Rnf=subs(diff(f,x,n+1),x,xi)*x^(n+1)/factorial(n+1)

%% 
% *P2*. Să se determine numărul natural $n$, astfel ca pentru $a = 0$ şi $f:\mathbb{R}\rightarrow\mathbb{R}$, 
% $f(x)=e^x$ $(T_nf)$ să aproximeze $f$ în $[-1,1]$ cu trei zecimale exacte.

syms n rest(n)
rest(n)=3/factorial(n+1);
for k=3:10
    r=vpa(rest(k));
    disp([k,r])
    if abs(double(r))<1e-3, break; end
end
%% 
% Deci, $n=6$ este suficient

%k=k-1;
dezvT=taylor(exp(x),x,0,'Order',k+1)
vpa(subs(dezvT,x,1.0),10)
exp(1)
%% 
% *P3*. Să se aproximeze $\sqrt[3]{999}$ cu 12 zecimale exacte.

dg=digits;
digits(13);
syms f(x) R(n) ex
ex=sym(1)/sym(3);
f(x)=10*(1-x)^(sym(1)/sym(3));
R(n)=vpa(nchoosek(ex,n)*10^(-3*n));
R(1)
R(2)
R(3)
R(4)
%% 
% $n=4$ este suficient

P=taylor(f,x,0,'Order',4)
digits(dg)
vpa(subs(P,x,0.001))
vpa(999^ex)
%% 
%