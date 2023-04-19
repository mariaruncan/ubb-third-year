function c = barycentricWeigths(x)
    n = length(x);
    c = ones(1, n);
    for j=1:n
        c(j) = prod(x(j) - x([1:j-1, j+1:n]));
    end
    c=1./c;
end
