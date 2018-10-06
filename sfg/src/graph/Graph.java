package graph;

import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.Stack;

public class Graph {
	double[][] adjMatrix;
	ArrayList<Stack<Integer>> cycles = new ArrayList<Stack<Integer>>();
	int size;
	mxGraph mx = new mxGraph();
	public Object mxNodes[];
	Object parent = mx.getDefaultParent();

	public Graph(int s) {
		size = s;
		adjMatrix = new double[s][s];
		mxNodes = new Object[size];
		int x = 20;
		int y = 100;
		for (int i = 0; i < mxNodes.length; i++) {
			mxNodes[i] = mx
					.insertVertex(
							parent,
							null,
							"y" + i,
							x,
							y,
							25,
							25,
							"rounded=1;curved=1;shape=ellipse;fillColor=#FAFAFA;strokeColor=#222130;fontColor=#222130;strokeWidth=1;verticalAlign=bottom;");
			x += 920 / (size-1);
			y += 50;
		}
	}

	public mxGraph getMxGraph() {
		return mx;
	}

	public void addEdge(int src, int target, double value) {
		if (src > size || target > size) {
			System.out.println("Node does not exist!");
			return;
		}
		adjMatrix[src][target] += value;
		removeEdge(src, target);
		insertEdge(src, target, adjMatrix[src][target], "#222130",1);
		ArrayList<Stack<Integer>> paths = findPaths(target, src);
		if(adjMatrix[src][target] == 0){
			removeEdge(src, target);
		}
		if (!paths.isEmpty()) {
			for (int i = 0; i < paths.size(); i++) {
				Stack<Integer> currentPath = paths.get(i);
				currentPath.push(target);
				cycles.add(paths.get(i));
			}
		}
		if (src == target) {
			Stack<Integer> st = new Stack<Integer>();
			st.push(src);
			st.push(target);
			cycles.add(st);
		}
	}

	public ArrayList<Stack<Integer>> getCycles() {
		return cycles;
	}

	public void printGraph() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (adjMatrix[i][j] > 0) {
					System.out.println(i + " --- " + adjMatrix[i][j]
							+ " ----> " + j);
				}
			}
		}
	}

	public ArrayList<Stack<Integer>> findForwardPaths(int src, int target) {
		ArrayList<Stack<Integer>> paths = new ArrayList<Stack<Integer>>();
		Stack<Integer> path = new Stack<Integer>();
		path.add(src);
		if (src >= size || target >= size) {
			System.out.println("Node does not exist!");
			return null;
		}
		boolean changeJ = false;
		int jValue = 0;
		for (int i = src; i <= target; i++) {
			if (!path.isEmpty()) {
				i = path.peek();
			} else {
				break;
			}
			boolean found = false;
			for (int j = i + 1; j <= target; j++) {
				if (changeJ) {
					j = jValue;
				}
				changeJ = false;
				if (i < size && j < size && adjMatrix[i][j] != 0 ) {
					found = true;
					path.add(j);
					i = j;
					if (j == target) {
						@SuppressWarnings("unchecked")
						Stack<Integer> currentPath = (Stack<Integer>) path
								.clone();
						paths.add(currentPath);
						// path.pop();
						changeJ = true;
						jValue = path.pop() + 1;
						if (path.size() > 0) {
							i = path.peek();
						}
					}
				}
			}
			if (found == false) {
				changeJ = true;
				if (path.size() > 0) {
					jValue = path.pop() + 1;
					if (!path.isEmpty()) {
						i = path.peek();
					}
				} else {
					i = target + 1; // break from the big loop
				}
			}
		}
		return paths;
	}

	public ArrayList<Stack<Integer>> findPaths(int src, int target) {
		// System.out.println("Finding path between " + src + " and "
		// + target);
		ArrayList<Stack<Integer>> paths = new ArrayList<Stack<Integer>>();
		boolean changeJ = false;
		int jValue = 0;
		Stack<Integer> path = new Stack<Integer>();
		path.add(src);
		if (src >= size || target >= size) {
			System.out.println("Node does not exist!");
			return null;
		}
		int i = src;
		while (i != target && i < size) {
			if (!path.isEmpty()) {
				i = path.peek();
			} else {
				break;
			}
			boolean found = false;
			for (int j = 0; j < size; j++) {
				if (changeJ) {
					j = jValue;
					changeJ = false;
				}
				if (path.contains(j)) {
					continue;
				}

				if (i < size && j < size && adjMatrix[i][j] != 0) {
					found = true;
					path.add(j);
					i = j;
					
					if (j == target) {
						@SuppressWarnings("unchecked")
						Stack<Integer> currentPath = (Stack<Integer>) path
								.clone();
						paths.add(currentPath);
						if (!path.isEmpty()) {
							changeJ = true;
							jValue = path.pop() + 1;
							i = path.peek();
							break;
						}
					}else{
						changeJ = true;
						jValue = 0;
					}
					
				}
			}
			if (found == false) {
				changeJ = true;
				if (!path.isEmpty()) {
					jValue = path.pop() + 1;

					if (!path.isEmpty()) {
						i = path.peek();
					}
				} else {
					i = target + 1; // break from the big loop
				}
			}
		}
		return paths;
	}

	public void printMatrix() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(adjMatrix[i][j] + "   ");
			}
			System.out.println();
		}
	}

	ArrayList<Stack<Integer>> getForwardPaths() {
		return findForwardPaths(0, size - 1);
	}

	public double getPathGain(Stack<Integer> path) {
		double gain = 1;
		@SuppressWarnings("unchecked")
		Stack<Integer> pathCopy = (Stack<Integer>) path.clone();
		while (pathCopy.size() >= 2) {
			Integer n1 = pathCopy.pop();
			Integer n2 = pathCopy.peek();
			gain *= adjMatrix[n2][n1];
		}
		return gain;
	}

	public boolean isNonTouching(Stack<Integer> loop1, Stack<Integer> loop2) {
		@SuppressWarnings("unchecked")
		Stack<Integer> loop1Copy = (Stack<Integer>) loop1.clone();
		while (!loop1Copy.isEmpty()) {
			if (loop2.contains(loop1Copy.pop())) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<Stack<Integer>> addNextCycle(int startPoint,
			ArrayList<Stack<Integer>> currentCombination) {
		ArrayList<Stack<Integer>> tempCombination = currentCombination;
		for (int i = startPoint; i < cycles.size(); i++) {
			boolean isOk = false;
			for (int j = 0; j < currentCombination.size(); j++) {
				if (isNonTouching(cycles.get(i), currentCombination.get(j))) {
					isOk = true;
				} else {
					isOk = false;
					break;
				}
			}
			if (isOk) {
				tempCombination.add(cycles.get(i));
				return tempCombination;
			}
		}
		return null;
	}

	public ArrayList<ArrayList<Stack<Integer>>> get2NonTouchingLoops() {
		ArrayList<ArrayList<Stack<Integer>>> twoNonTouchings = new ArrayList<ArrayList<Stack<Integer>>>();

		for (int i = 0; i < cycles.size(); i++) {
			for (int j = i + 1; j < cycles.size(); j++) {
				if (isNonTouching(cycles.get(i), cycles.get(j))) {
					ArrayList<Stack<Integer>> currentCombination = new ArrayList<Stack<Integer>>();
					currentCombination.add(cycles.get(i));
					currentCombination.add(cycles.get(j));
					twoNonTouchings
							.add((ArrayList<Stack<Integer>>) currentCombination);
				}
			}
		}
		return twoNonTouchings;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Stack<Integer>>> findNonTouchingLoops(int n) {
		ArrayList<ArrayList<Stack<Integer>>> nonTouchingLoops = new ArrayList<ArrayList<Stack<Integer>>>();
		if (n == 2) {
			nonTouchingLoops = get2NonTouchingLoops();
		} else {
			ArrayList<ArrayList<Stack<Integer>>> lowerNonTouchingLoops = findNonTouchingLoops(n - 1);
			for (int i = 0; i < lowerNonTouchingLoops.size(); i++) {
				ArrayList<Stack<Integer>> currentLoop = lowerNonTouchingLoops
						.get(i);
				for (int j = cycles
						.indexOf(currentLoop.get(currentLoop.size() - 1)); j < cycles
						.size(); j++) {
					currentLoop = lowerNonTouchingLoops.get(i);
					currentLoop = addNextCycle(j, currentLoop);
					if (!nonTouchingLoops.contains(currentLoop)
							&& currentLoop != null) {
						nonTouchingLoops
								.add((ArrayList<Stack<Integer>>) currentLoop
										.clone());
					}
					if (currentLoop != null)
						currentLoop.remove(currentLoop.size() - 1);
				}
			}
		}

		return nonTouchingLoops;
	}

	public double getSumOfGains(ArrayList<Stack<Integer>> loops) {
		double sum = 0;
		for (int i = 0; i < loops.size(); i++) {
			sum += getPathGain(loops.get(i));
		}
		return sum;
	}

	public double getGainProduct(ArrayList<Stack<Integer>> loops) {
		double product = 1;
		for (int i = 0; i < loops.size(); i++) {
			product *= getPathGain(loops.get(i));
		}
		return product;
	}

	public double getSumOfGainProducts(
			ArrayList<ArrayList<Stack<Integer>>> loopCombinations) {
		double sum = 0;
		for (int i = 0; i < loopCombinations.size(); i++) {
			sum += getGainProduct(loopCombinations.get(i));
		}
		return sum;
	}

	public double getDelta() {
		double sum = 0;
		double sumOfIndividualLoops = getSumOfGains(cycles);
		sum = 1 - sumOfIndividualLoops;
		int sign = 1;
		int n = 2;
		while (findNonTouchingLoops(n).size() > 0) {
			sum += sign * getSumOfGainProducts(findNonTouchingLoops(n));
			n++;
			sign *= -1;
		}
		return sum;
	}

	@SuppressWarnings("unchecked")
	public double getDelta(int i) {
		Stack<Integer> forwardPathI = (getForwardPaths()).get(i);

	//	Stack<Integer> forwardPathI = (findForwardPaths(0, size - 1)).get(i);
		int sign = 1;
		int n = 2;
		double sum = 0;
		ArrayList<Stack<Integer>> individualNonTouching = (ArrayList<Stack<Integer>>) cycles.clone();
		for (int j = 0; j < individualNonTouching.size(); j++) {
			if (!isNonTouching(individualNonTouching.get(j), forwardPathI)) {
				individualNonTouching.remove(j);
				j--;
			}
		}
		double sumOfIndividualLoops = getSumOfGains(individualNonTouching);
		sum = 1 - sumOfIndividualLoops;
		while (findNonTouchingLoops(n).size() > 0) {
			ArrayList<ArrayList<Stack<Integer>>> NTLs = (ArrayList<ArrayList<Stack<Integer>>>) findNonTouchingLoops(n).clone();
			for (int j = 0; j < NTLs.size(); j++) {
				for (int k = 0; k < NTLs.get(j).size(); k++) {
					if (!isNonTouching(NTLs.get(j).get(k), forwardPathI)) {
						NTLs.remove(j);
						j--;
						break;
					}
				}
			}
			sum += sign * getSumOfGainProducts(NTLs);
			n++;
			sign *= -1;
		}
		return sum;
	}

	double getTotalGain() {
		double totalGain = 0;
		ArrayList<Stack<Integer>> forwardPaths = getForwardPaths();
		for (int i = 0; i < forwardPaths.size(); i++) {
			totalGain += getDelta(i) * getPathGain(forwardPaths.get(i));
		}
		if (getDelta() != 0) {
			totalGain /= getDelta();
			return totalGain;
		}
		return 0;
	}

	public void insertEdge(int src, int target, double value, String color, int strokeWidth) {
		if (target == src || target == src + 1) {
			mx.insertEdge(parent, null, adjMatrix[src][target], mxNodes[src],
					mxNodes[target],
					"curved=1;rounded=1;labelPosition=left;fontColor=#00000;strokeColor="
							+ color + ";verticalAlign=bottom;strokeWidth="+strokeWidth);
		} else {
			mx.insertEdge(
					parent,
					null,
					adjMatrix[src][target],
					mxNodes[src],
					mxNodes[target],
					"curved=1;rounded=1;edgeStyle=segmentEdgeStyle;segment=15;labelPosition=left;fontColor=#00000;strokeColor="
							+ color + ";verticalAlign=bottom;strokeWidth="+strokeWidth);

		}

	}

	public void setPathColor(Stack<Integer> path, String color) {
		@SuppressWarnings("unchecked")
		Stack<Integer> pathCopy = (Stack<Integer>) path.clone();
		ArrayList<Stack<Integer>> edges = new ArrayList<Stack<Integer>>();
		while (pathCopy.size() >= 2) {
			Integer n1 = pathCopy.pop();
			Integer n2 = pathCopy.peek();
			Stack<Integer> st = new Stack<Integer>();
			st.push(n2);
			st.push(n1);
			edges.add(st);
		}
		removeEdges(edges);
		for(int i = 0 ; i < edges.size() ; i ++){
			int target = edges.get(i).pop();
			int src = edges.get(i).pop();
			insertEdge(src, target,adjMatrix[src][target], color,2);
		}
		
	}
	
	@SuppressWarnings({ "unchecked" })
	public void setPathsColor(ArrayList<Stack<Integer>> paths, String color){
		ArrayList<Stack<Integer>> edges = new ArrayList<Stack<Integer>>();
		for (int i = 0; i < paths.size(); i++) {
			Stack<Integer> st = (Stack<Integer>) paths.get(i).clone();
			while(st.size() > 1){
				Stack<Integer> currentPair = new Stack<Integer>();
				Integer target = st.pop();
				Integer src = st.peek();
				currentPair.push(src);
				currentPair.push(target);
				edges.add(currentPair);
				System.out.println("");
			}
		}
		removeEdges(edges);
		for (int i = 0; i < edges.size(); i++) {
			Stack<Integer> currentPair = edges.get(i);
			int target = currentPair.pop();
			int src = currentPair.peek();
			insertEdge(src, target, adjMatrix[src][target], color ,2);
		}
	}

	public void removeEdge(int src, int target) {
		Object toRemove[] = { mxNodes[src], mxNodes[target] };
		mx.removeCells(toRemove);
		mx.addCells(toRemove);
		for (int i = 0; i < adjMatrix.length; i++) {
			
			if (i != target && adjMatrix[src][i] != 0) {
				insertEdge(src, i, adjMatrix[src][i], "#222130",1);
			}
			if (i != target && adjMatrix[i][src] != 0) {
				insertEdge(i, src, adjMatrix[i][src], "#222130",1);
			}
			if (i != src && adjMatrix[i][target] != 0) {
				insertEdge(i, target, adjMatrix[i][target], "#222130",1);
			}
			if (target!= src && adjMatrix[target][i] != 0) {
				insertEdge(target, i, adjMatrix[target][i], "#222130",1);
			}

		}
	}

	public void removeEdges(ArrayList<Stack<Integer>> edges) {
		mx.removeCells(mxNodes);
		mx.addCells(mxNodes);
		boolean removeCell[][] = new boolean[size][size];
		///
		for(int i = 0 ; i < edges.size() ; i++){
			int target = edges.get(i).pop();
			int src = edges.get(i).peek();
			edges.get(i).push(target);
			removeCell[src][target] = true;
		}
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix.length; j++) {
				if (adjMatrix[i][j] != 0 && !removeCell[i][j]) {
					insertEdge(i, j, adjMatrix[i][j], "#222130",1);
				}
			}
		}
	}
}
