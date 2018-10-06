package graph;

import java.awt.Color;
import java.awt.Insets;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.*;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class InputGetter extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String strNodes;
	JLabel logo = new JLabel(new ImageIcon(
			"logoResized.png"));
	JLabel nodesNumLbl;
	JLabel startNodeLbl = new JLabel("Start node: ");
	JLabel endNodeLbl = new JLabel("Target node: ");
	JLabel gainLb = new JLabel("Gain: ");
	JLabel gainResultLbl = new JLabel("TF = ");
	JLabel pathGainLbl = new JLabel("");
	JLabel nonTouchingsLbl1 = new JLabel("Display ");
	JLabel nonTouchingsLbl2 = new JLabel("non touching loops ");
	JLabel nonTouchingsResultLbl = new JLabel("");

	JComboBox<String> nodesStart;
	JComboBox<String> nodesTarget;
	JComboBox<String> mode;

	JButton newGraph = new JButton("New Graph");
	JButton okBtn = new JButton("Ok");
	JButton addEdgeBtn = new JButton("Add Edge");
	JButton swapBtn = new JButton("⇄");
	JButton getGainBtn = new JButton("Transfer Function");
	JButton showCyclesBtn = new JButton("Show Cycles");
	JButton showForwardPathsBtn = new JButton("Show Forward Paths");
	JButton nonTouchingsBtn = new JButton("Display");
	JButton increaseSrcBtn = new JButton("+1");
	JButton increaseTargetBtn = new JButton("+1");

	JTextField numOfNodesTF = new JTextField("");
	JTextField gainTF = new JTextField("");
	JTextField nonTouchingsTF = new JTextField();

	JFrame sfg = new JFrame("Graph Illustration");
	JFrame cyclesJF = new JFrame("");
	JFrame NNTLsJF = new JFrame("");
	JFrame FPsJF = new JFrame("");

	JTable cyclesTbl = new JTable();

	Font gothic = new Font("URW Gothic L", Font.BOLD, 15);
	mxGraph graph = new mxGraph();

	boolean setVisible = true;
	Graph g;

	int cycleIndex = -1;
	int fpIndex = -1;
	int cycleSetIndex = -1;
	String currentColor;

	public InputGetter() {
		buildGUI();
		setLayout(null);
		graph.getModel().beginUpdate();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		InputGetter ig = new InputGetter();

	}

	public void buildGUI() {
		setLayout(null);
		nodesNumLbl = new JLabel("Number of nodes:");
		nodesNumLbl.setFont(new Font("URW Gothic L", Font.BOLD, 16));
		nodesNumLbl.setForeground(Color.decode("#227093"));
		nodesNumLbl.setBounds(300, 100, 200, 50);
		logo.setBounds(10, 10, 800, 100);
		okBtn.setBounds(350, 210, 100, 50);
		numOfNodesTF.setBounds(300, 140, 200, 50);
		add(logo);
		add(nodesNumLbl);
		add(okBtn);
		add(numOfNodesTF);
		setSize(780, 400);
		getContentPane().setBackground(Color.decode("#f5f6fa"));
		setVisible(true);
		swapBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int temp = nodesTarget.getSelectedIndex();
				nodesTarget.setSelectedIndex(nodesStart.getSelectedIndex());
				nodesStart.setSelectedIndex(temp);
			}
		});
		increaseSrcBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int temp = nodesStart.getSelectedIndex();
				nodesStart.setSelectedIndex((temp + 1) % g.size);
			}
		});

		increaseTargetBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int temp = nodesTarget.getSelectedIndex();
				nodesTarget.setSelectedIndex((temp + 1) % g.size);
			}
		});

		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (isNumeric(numOfNodesTF.getText())
						&& numOfNodesTF.getText() != null) {
					setSize(1200, 480);
					strNodes = numOfNodesTF.getText();
					int nodesNum = Integer.parseInt(strNodes);
					g = new Graph(nodesNum);
					String nodesNames[] = new String[nodesNum];
					for (int i = 0; i < nodesNames.length; i++) {
						nodesNames[i] = "" + i;
					}
					logo.setVisible(false);
					logo = new JLabel(new ImageIcon(
							"header.png"));
					logo.setVisible(true);
					nodesStart = new JComboBox<>(nodesNames);
					nodesTarget = new JComboBox<>(nodesNames);

					newGraph.setFont(new Font("Droid Sans", Font.PLAIN, 18));
					newGraph.setFocusPainted(false);

					newGraph.setBackground(Color.decode("#273c75"));
					newGraph.setBorder(null);
					newGraph.setForeground(Color.white);

					String[] modes = { "Highlight", "List" };
					mode = new JComboBox<>(modes);

					// setting bounds
					logo.setBounds(0, 0, 1200, 110);
					newGraph.setBounds(1000, 0, 180, 70);
					startNodeLbl.setBounds(100, 70, 300, 50);
					mode.setBounds(900, 380, 200, 50);
					nodesStart.setBounds(100, 110, 200, 50);
					endNodeLbl.setBounds(600, 70, 300, 50);
					nodesTarget.setBounds(600, 110, 200, 50);
					gainLb.setBounds(900, 70, 200, 50);
					gainTF.setBounds(900, 110, 200, 50);
					addEdgeBtn.setBounds(600, 220, 160, 50);
					getGainBtn.setBounds(600, 290, 160, 50);
					swapBtn.setBounds(440, 110, 50, 50);
					showCyclesBtn.setBounds(100, 220, 200, 50);
					showForwardPathsBtn.setBounds(100, 290, 200, 50);
					swapBtn.setFont(new Font("Arial", Font.PLAIN, 20));
					swapBtn.setMargin(new Insets(0, 0, 0, 0));
					pathGainLbl.setBounds(780, 220, 200, 50);
					nonTouchingsLbl1.setBounds(260, 380, 80, 50);
					nonTouchingsTF.setBounds(320, 380, 80, 50);
					nonTouchingsLbl2.setBounds(410, 380, 170, 50);
					nonTouchingsBtn.setBounds(630, 380, 100, 50);
					increaseSrcBtn.setBounds(310, 110, 30, 50);
					increaseTargetBtn.setBounds(810, 110, 30, 50);

					increaseSrcBtn.setMargin(new Insets(0, 0, 0, 0));
					increaseTargetBtn.setMargin(new Insets(0, 0, 0, 0));

					showCyclesBtn.setFont(gothic);
					showForwardPathsBtn.setFont(gothic);
					addEdgeBtn.setFont(gothic);
					getGainBtn.setFont(gothic);

					// adding components
					add(logo);
					add(newGraph);
					add(startNodeLbl);
					add(endNodeLbl);
					add(gainLb);
					add(nodesStart);
					add(nodesTarget);
					add(gainTF);
					add(addEdgeBtn);
					add(getGainBtn);
					add(gainResultLbl);
					add(swapBtn);
					add(showCyclesBtn);
					add(showForwardPathsBtn);
					add(pathGainLbl);
					add(nonTouchingsLbl1);
					add(nonTouchingsTF);
					add(nonTouchingsLbl2);
					add(nonTouchingsBtn);
					add(increaseSrcBtn);
					add(increaseTargetBtn);
					add(mode);

					nodesNumLbl.setVisible(false);
					okBtn.setVisible(false);
					numOfNodesTF.setVisible(false);
					repaint();
				}
			}
		});
		addEdgeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isNumeric(gainTF.getText())) {
					double gain = Double.parseDouble(gainTF.getText());
					int startNode = nodesStart.getSelectedIndex();
					int endNode = nodesTarget.getSelectedIndex();
					g.addEdge(startNode, endNode, gain);
					g.printGraph();
					graph = g.getMxGraph();
					sfg.setVisible(true);
					// sfg = new JFrame("GUI Signal flow graph");
					mxGraphComponent graphComponent = new mxGraphComponent(
							graph);
					sfg.add(graphComponent);
					// sfg.setLayout(null);
					// graphComponent.setBounds(10,10,1000,500);
					if (setVisible) {
						sfg.setVisible(true);
						setVisible = false;
						sfg.setSize(1040, 600);
					}
				}
			}
		});
		getGainBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				gainResultLbl.setBounds(780, 290, 270, 50);
				gainResultLbl.setText("TF = " + g.getTotalGain());
				add(gainResultLbl);
			}
		});

		showCyclesBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// System.out.println(g.getCycles());
				if (mode.getSelectedIndex() == 0) {
					cycleIndex++;
					if (cycleIndex < g.getCycles().size()) {
						g.setPathColor(g.getCycles().get(cycleIndex), "#d63031");
					} else if (g.getCycles().size() > 0) {
						cycleIndex = 0;
						g.setPathColor(g.getCycles().get(cycleIndex), "#d63031");
					}
					if (cycleIndex < g.getCycles().size()) {
						pathGainLbl.setText("Gain = "
								+ g.getPathGain(g.getCycles().get(cycleIndex)));
					}
				} else {
					String[] colLbl = new String[1];
					colLbl[0] = "1";
					cyclesJF.setTitle("Loops");
					cyclesTbl = new JTable(toStringArray(g.getCycles()), colLbl);
					cyclesJF.setSize(500, 500);
					cyclesJF.add(cyclesTbl);
					cyclesJF.setVisible(true);
				}
			}
		});

		showForwardPathsBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (mode.getSelectedIndex() == 0) {
					fpIndex++;
					if (fpIndex < g.getForwardPaths().size()) {
						g.setPathColor(g.getForwardPaths().get(fpIndex),
								"#0984e3");
					} else if (g.getForwardPaths().size() > 0) {
						fpIndex = 0;
						g.setPathColor(g.getForwardPaths().get(fpIndex),
								"#0984e3");
					}
					if (fpIndex < g.getForwardPaths().size()) {
						double delta = g.getDelta(fpIndex);
						pathGainLbl.setText("Gain = "
								+ g.getPathGain(g.getForwardPaths()
										.get(fpIndex)) + " , Delta = " + delta);
					}
				}
				if (mode.getSelectedIndex() == 1) {
					String[] colLbl = new String[1];
					colLbl[0] = "1";
					cyclesTbl = new JTable(toStringArray(g.getForwardPaths()),
							colLbl);
					FPsJF.setSize(500, 500);
					FPsJF.add(cyclesTbl);
					FPsJF.setTitle("Forward Paths");
					FPsJF.setVisible(true);
				}
			}
		});

		nonTouchingsBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int n;
				if (isNumeric(nonTouchingsTF.getText())) {
					n = Integer.parseInt(nonTouchingsTF.getText());
					if (n > 1) {
						String[][] loops = new String[g.findNonTouchingLoops(n)
								.size()][n];
						String[] colLbl = new String[n];
						for (int i = 0; i < loops.length; i++) {
							for (int j = 0; j < loops[i].length; j++) {
								loops[i][j] = g.findNonTouchingLoops(n).get(i)
										.get(j).toString();
							}
						}
						for (int i = 0; i < n; i++) {
							colLbl[i] = i + "";
						}
						cyclesTbl = new JTable(loops, colLbl);

						NNTLsJF.setSize(600, 500);
						NNTLsJF.add(cyclesTbl);
						if (mode.getSelectedIndex() == 1) {
							NNTLsJF.setTitle(n + " Non-Touching Loops");
							NNTLsJF.setVisible(true);
						}
					}
					if (mode.getSelectedIndex() == 0) {
						cycleSetIndex++;
						if (cycleSetIndex < g.findNonTouchingLoops(n).size()) {
							g.setPathsColor(
									g.findNonTouchingLoops(n)
											.get(cycleSetIndex), "#f19066");
						} else if (g.findNonTouchingLoops(n).size() > 1) {
							cycleSetIndex = -1;
						}
					}
				}
			}
		});
		newGraph.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				sfg.setVisible(false);
				cyclesJF.setVisible(false);
				new InputGetter();
			}
		});
		newGraph.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				newGraph.setBackground(Color.decode("#7f8fa6"));
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				newGraph.setBackground(Color.decode("#273c75"));
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	String[][] toStringArray(ArrayList<Stack<Integer>> paths) {
		String[][] result = new String[paths.size()][1];
		for (int i = 0; i < result.length; i++) {
			result[i][0] = paths.get(i).toString();
		}
		return result;
	}
}
