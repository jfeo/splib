function [data, f] = process(filename)
set(0,'defaulttextinterpreter','latex');
set(0,'DefaultFigureWindowStyle','docked');

data.tab = readtable(filename, 'Delimiter', '\t', 'Format', '%s%d%d%d%d%d');
data.mat = table2array(data.tab(:, 2:end));
data.names = table2array(data.tab(:, 1));
data.unames = unique(data.names);
data.unameidx = arrayfun(@(d) find(strcmp(data.unames, d)), data.names);

f = figure;
hold on;
grid on;

for i = 1:length(data.unames)
    scatter3(data.mat(data.unameidx == i, 2), data.mat(data.unameidx == i, 3), data.mat(data.unameidx == i, 5), 'x', 'DisplayName', char(data.unames(i)));
end

legend('show');
title('Experimental time complexity of Dijkstras algorithm');
xlabel('Vertices');
ylabel('Edges');
zlabel('Time (ns)');

hold off;

end