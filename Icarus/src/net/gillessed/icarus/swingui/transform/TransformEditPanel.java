package net.gillessed.icarus.swingui.transform;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import net.gillessed.icarus.geometry.Triangle;
import net.gillessed.icarus.swingui.FlameModelContainer;

public class TransformEditPanel extends TransformShowPanel {
	private static final long serialVersionUID = -7644806075702546344L;
	private final MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if(triangleDragged >= 0 ) {
				triangleDragged = - 1;
				moveState = 0;
				flameModelContainer.flameModified();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			for(Triangle t : triangles) {
				int result = t.checkMouse(e.getX(), e.getY());
				if(result != 0) {
					triangleDragged = triangles.indexOf(t);
					moveState = result;
					triangles.get(lastTouched).setLastTouched(false);
					t.setLastTouched(true);
					lastTouched = triangles.indexOf(t);
					break;
				}
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
	};

	private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseMoved(MouseEvent e) {
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if(triangleDragged >= 0)
			{
				switch(moveState)
				{
					case 1: triangles.get(triangleDragged).setA(e.getX(),e.getY()); break;
					case 2: triangles.get(triangleDragged).setB(e.getX(),e.getY()); break;
					case 3: triangles.get(triangleDragged).setC(e.getX(),e.getY()); break;
					default: System.err.println("Should not ever have a moveState of 0 when triangleDragged is not 0 in TransformEditPanel"); System.exit(0); break;
				}
				repaint();
			}
		}
	};

	public TransformEditPanel(FlameModelContainer flameModelContainer) {
		super(flameModelContainer, true);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseMotionListener);
	}
}
