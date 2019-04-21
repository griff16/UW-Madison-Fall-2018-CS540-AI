function CreateMap
    fid = fopen('result.txt');
    line = fgetl(fid);  % read line excluding newline character
    x = line;
    while ischar(line)
        line = fgetl(fid);
        if line == -1 
            break;
        end
        x = [x, line];
    end
    
    corpus = strsplit(x);
    for i = 1 : 3 : size(corpus, 2)
        hold on;
        disp(i)
        figure(1)
        scatter(str2double(corpus(1, i)), str2double(corpus(1, i + 2)));
        figure(2)
        scatter(log10(str2double(corpus(1, i))), log10(str2double(corpus(1, i + 2))));
    end 
    fclose(fid);
end 