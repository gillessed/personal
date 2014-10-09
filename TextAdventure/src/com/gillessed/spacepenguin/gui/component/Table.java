package com.gillessed.spacepenguin.gui.component;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.gillessed.spacepenguin.gui.GraphicsUtils;
import com.gillessed.spacepenguin.gui.RenderTarget;
import com.gillessed.spacepenguin.gui.renderproperties.RenderProperties;
import com.gillessed.spacepenguin.gui.renderproperties.SPBorder;

public class Table extends RenderTarget {

	private Div[][] cells;
	private RenderTarget[][] elements;
	private int columns;
	private int rows;
	
	public Table(String id, int columns, int rows) {
		super(id);
		this.columns = columns;
		this.rows = rows;
		
		elements = new RenderTarget[columns][rows];
		cells = new Div[columns][rows];
		for(int i = 0; i < columns; i++) {
			for(int j = 0; j < rows; j++) {
				final int i_ = i;
				final int j_ = j;
				cells[i][j] = new Div(id + "_cell_" + i + "_" + j) {
					@Override
					protected Dimension getInnerDimension(Graphics2D g) {
						return new Dimension(getColumnWidth(i_), getRowHeight(j_));
					}
				};
				cells[i][j].addClass("table_div");
				cells[i][j].addClass("table_" + id + "_div");
				addChild(cells[i][j]);
			}
		}
	}
	
	public void addElement(RenderTarget rt, int column, int row) {
		if(row < 0 || row >= rows || column < 0 || column >= columns){
			throw new RuntimeException("Specified cell [row=" + row + ",column=" + column + " doesn't exist.");
		}
		if(elements[column][row] != null) {
			cells[column][row].removeChild(elements[column][row]);
			elements[column][row] = null;
		}
		elements[column][row] = rt;
		cells[column][row].addChild(rt);
	}
	
	public void removeElement(RenderTarget rt, int column, int row) {
		if(row < 0 || row >= columns || column < 0 || column >= rows){
			throw new RuntimeException("Specified cell [row=" + row + ",column=" + column + " doesn't exist.");
		}
		if(elements[column][row] != null) {
			cells[column][row].removeChild(elements[column][row]);
			elements[column][row] = null;
		}
	}
	
	public int getColumnWidth(int column) {
		int maxWidth = 0;
		for(int j = 0; j < rows; j++) {
			if(elements[column][j] != null) {
				Rectangle r = elements[column][j].getMargin();
				if(r.width > maxWidth) {
					maxWidth = r.width;
				}
			}
		}
		return maxWidth;
	}
	
	public int getRowHeight(int row) {
		int maxHeight = 0;
		for(int i = 0; i < columns; i++) {
			if(elements[i][row] != null) {
				Rectangle r = elements[i][row].getMargin();
				if(r.height > maxHeight) {
					maxHeight = r.height;
				}
			}
		}
		return maxHeight;
	}
	
	@Override
	public void update(Graphics2D g) {
		for(int i = 0; i < columns; i++) {
			for(int j = 0; j < rows; j++) {
				if(elements[i][j] != null) {
					elements[i][j].update(g);
				}
			}
		}
		super.update(g);
	}
	
	@Override
	protected Dimension getInnerDimension(Graphics2D g) {
		int dimWidth = 0;
		int dimHeight = 0;
		if(columns > 0 && rows > 0) {
			for(int i = 0; i < columns; i++) {
				dimWidth += getColumnWidth(i);
			}
			for(int j = 0; j < rows; j++) {
				dimHeight += getRowHeight(j);
			}
			RenderProperties rp = getRenderProperties();
			SPBorder border = rp.getBorder(getState());
			dimWidth += border.thickness * (elements.length);
			dimHeight += border.thickness * (elements[0].length);
		}
		return new Dimension(dimWidth, dimHeight);
	}
	
	@Override
	public Rectangle draw(Graphics2D g) {
		super.draw(g);
		RenderProperties rp = getRenderProperties();
		SPBorder border = rp.getBorder(getState());
		GraphicsUtils.setStrokeForBorder(g, border);
		int x = inner.x;
		int y = inner.y;
		if(border.thickness > 0) {
			for(int i = 0; i <= rows; i++) {
				g.drawLine(x, y, x + inner.width, y);
				if(i < rows) {
					y += getRowHeight(i) + border.thickness;
				}
			}
			x = inner.x;
			y = inner.y;
			for(int j = 0; j <= columns; j++) {
				g.drawLine(x, y, x, y + inner.height);
				if(j < columns) {
					x += getColumnWidth(j) + border.thickness;
				}
			}
		}
		x = inner.x + border.thickness;
		y = inner.y + border.thickness;
		for(int i = 0; i < columns; i++) {
			y = inner.y + border.thickness;
			for(int j = 0; j < rows; j++) {
				if(elements[i][j] != null) {
					lastDraw = new Rectangle(x, y, 0, 0);
					cells[i][j].update(g);
					cells[i][j].draw(g);
				}
				y += getRowHeight(j) + border.thickness;
			}
			x += getColumnWidth(i) + border.thickness;
		}
		lastDraw = null;
		return margin;
	}
}
