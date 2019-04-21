function Cluster
    X = [42.4 71.1;... 
        41.7 74.0;...
        38.9 77.0;...
        25.8 80.2;...
        40.8 111.9;...
        47.6 122.3;...
        37.8 122.4;...
        34.1 118.2;...
        39.7 105.0;...
        33.7 84.3];
    
    a = [41 74.0333];
    b = [37.07143 106.3286];
%     Y = pdist(X);
%     squareform(Y);
%     
%     Z = linkage(Y);
%     dendrogram(Z)
%     X

    groupA = [];
    groupB = [];
    
    for i = 1 : size(X,1)
        one = calDist(X(i, 1), X(i, 2), a(1), a(2));
        two = calDist(X(i, 1), X(i, 2), b(1), b(2));
        
        disp("one:"+ one + ", two:"+two);
        if one < two
            groupA = [groupA,i];
        else
            groupB = [groupB, i];
        end 
    end
    
%     disp("A: "+ sum(groupA)/size(groupA,1))
%     disp("B: "+ sum(groupB)/size(groupB,1))
    disp(groupA)
    disp(groupB)
end 

function result = calDist (a, b, c, d)
    X = [a b;c d];
%     fprintf("a:%f, b:%f, c:%f, d:%f\n", a,b,c,d)
    result = pdist(X);
end