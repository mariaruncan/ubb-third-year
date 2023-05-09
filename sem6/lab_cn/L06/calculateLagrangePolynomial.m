function P=calculateLagrangePolynomial(x, y, xx, has_plot)
if nargin < 4, has_plot = false; end

    P = zeros(size(xx));
    m = length(x);
    
    if has_plot
        hold on
        xlabel('k')
        ylabel('l_k')
    end
    
    for k=1:m
        L = ones(size(xx));
        for j=1:m
            if k~=j
                L = L .* ((xx - x(j)) / (x(k) - x(j)));
            end
        end

        if has_plot
            if length(L) == 2
                plot(x(k), L(1), 'bo')
                plot(x(k), L(2), 'r*')
            else
                plot(k, L, 'bo')
            end
        end

        P = P + L * y(k);
    end

    if has_plot
        hold off
    end
end