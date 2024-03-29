function Q = quadstep(f, a, b, prec, fa, fc, fb)
    h = b - a; 
    c = (a + b) / 2;
    fd = f((a + c) / 2);
    fe = f((c + b) / 2);
    Q1 = h/6 * (fa + 4*fc + fb);
    Q2 = h/12 * (fa + 4*fd + 2*fc + 4*fe + fb);

    if abs(Q2 - Q1) <= prec
        Q  = Q2 + (Q2 - Q1) / 15;
    else
        Qa = quadstep(f, a, c, prec, fa, fd, fc);
        Qb = quadstep(f, c, b, prec, fc, fe, fb);
        Q  = Qa + Qb;
    end
end