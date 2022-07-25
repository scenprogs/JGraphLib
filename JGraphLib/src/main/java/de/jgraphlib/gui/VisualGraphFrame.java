package de.jgraphlib.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;

import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.gui.style.VisualGraphStyle;

public class VisualGraphFrame<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> extends JFrame {

	private static final long serialVersionUID = 1L;
	VisualGraphPanel<V, E, W> visualGraphPanel;
	TerminalPanel terminal;

	public VisualGraphFrame(Weighted2DGraph<V, E, W> graph, VisualGraphStyle<E,W> style) {
		
		this.setLayout(new BorderLayout());
		this.visualGraphPanel = new VisualGraphPanel<V, E, W>(graph, style);
		this.visualGraphPanel.setFont(new Font("NotoSans", Font.PLAIN, 14));
		this.visualGraphPanel.setLayout(new OverlayLayout(visualGraphPanel));
		
		this.terminal = new TerminalPanel(new Font("Monospace", Font.PLAIN, 16), Color.WHITE);
		this.terminal.setFont(new Font("Consolas", Font.PLAIN, 16));
		this.terminal.setOpaque(false);
		this.terminal.setBackground(new Color(0, 0, 0, 100));
		this.terminal.setVisible(false);
		this.terminal.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				terminal.textArea.requestFocus();
			}
		});
		
		this.visualGraphPanel.add(this.terminal);
		this.add(visualGraphPanel);
		
		this.visualGraphPanel.getInputMap(
				JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"),
				VisualGraphFrameActions.TOGGLE_TERMINAL);
		
		this.visualGraphPanel.getActionMap().put(
				VisualGraphFrameActions.TOGGLE_TERMINAL,
				new VisualGraphFrameAction(VisualGraphFrameActions.TOGGLE_TERMINAL));
	}

	public void scale(double scaleFactor) {
		/*Font font = visualGraphPanel.getFont();
		visualGraphPanel.setFont(new Font(font.getName(), font.getStyle(), (int) (font.getSize() * scaleFactor)));
		visualGraphPanel.setVertexWidth((int) (visualGraphPanel.getVertexWidth() * scaleFactor));
		visualGraphPanel.repaint();

		font = terminal.getTextArea().getFont();
		terminal.getTextArea().setFont(new Font(font.getName(), font.getStyle(), (int) (font.getSize() * scaleFactor)));
		terminal.repaint();*/
	}

	public VisualGraphPanel<V, E, W> getVisualGraphPanel() {
		return this.visualGraphPanel;
	}

	public TerminalPanel getTerminal() {
		return this.terminal;
	}

	private enum VisualGraphFrameActions {
		TOGGLE_TERMINAL;
	}

	private class VisualGraphFrameAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		VisualGraphFrameActions action;

		public VisualGraphFrameAction(VisualGraphFrameActions action) {
			this.action = action;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (action) {
			case TOGGLE_TERMINAL:
				if (terminal.isVisible()) {
					terminal.setVisible(false);
				} else {
					terminal.setVisible(true);
				}
				break;
			default:
				break;
			}
		}
	}
}
