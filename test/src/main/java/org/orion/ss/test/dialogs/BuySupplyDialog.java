package org.orion.ss.test.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.service.GameService;
import org.orion.ss.service.ManagementService;
import org.orion.ss.service.ServiceFactory;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.test.components.FastPanel;
import org.orion.ss.test.components.SupplyDisplayer;
import org.orion.ss.test.validation.DoubleValidator;
import org.orion.ss.utils.NumberFormats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuySupplyDialog extends JDialog {

	private static final long serialVersionUID = -3709849519497626262L;
	protected final static Logger logger = LoggerFactory.getLogger(BuySupplyDialog.class);

	private final GameService gameService;
	private final ManagementService managementService;

	private final SupplyDisplayer parent;

	private JTextField[] supplyBuyTFs;
	private JButton buyB;

	public BuySupplyDialog(SupplyDisplayer parent, GameService gameService, Location location) {
		super();
		this.parent = parent;
		this.gameService = gameService;
		managementService = ServiceFactory.getManagementService(gameService.getGame());
		setTitle("Buy supplies to place at " + location);
		setBounds((int) GraphicTest.DEFAULT_DIALOG_LOCATION.getX(), (int) GraphicTest.DEFAULT_DIALOG_LOCATION.getY(), 400, 300);
		setModal(true);
		setLayout(null);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				setVisible(false);
				super.windowClosing(e);
			}

		});
		mount(location);
		setVisible(true);
	}

	protected void mount(final Location location) {
		FastPanel panel = new FastPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, getWidth(), getHeight());
		panel.addLabel("Supply type", GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		panel.addLabel("Prestige cost", GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		panel.addLabel("Amount", GraphicTest.LEFT_MARGIN * 3 + GraphicTest.COLUMN_WIDTH * 2, GraphicTest.TOP_MARGIN, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		final JTextField totalCostTF = new JTextField();
		totalCostTF.setText("0");
		supplyBuyTFs = new JTextField[SupplyType.values().length];
		int i = 1;
		for (SupplyType supply : SupplyType.values()) {
			panel.addLabel(supply.getDenomination(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
			panel.addNotEditableTextField("" + gameService.getGame().getCurrentPlayerPosition().getCountry().getMarket().get(supply), GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
			supplyBuyTFs[i - 1] = new JTextField();
			supplyBuyTFs[i - 1].setText("0");
			supplyBuyTFs[i - 1].setBounds(GraphicTest.LEFT_MARGIN * 3 + GraphicTest.COLUMN_WIDTH * 2, GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
			supplyBuyTFs[i - 1].addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {
				}

				@Override
				public void focusLost(FocusEvent arg0) {
					JTextField source = (JTextField) arg0.getSource();
					DoubleValidator validator = new DoubleValidator();
					validator.setMode(DoubleValidator.MODE_POSITIVE);
					if (validator.validate(source.getText())) {
						Stock stock = new Stock();
						for (int i = 0; i < supplyBuyTFs.length; i++) {
							stock.put(SupplyType.values()[i], Double.parseDouble(supplyBuyTFs[i].getText()));
						}
						int supplyCost = managementService.stockValue(stock, gameService.getGame().getCurrentPlayerPosition());
						totalCostTF.setText(NumberFormats.PRESTIGE.format(supplyCost));
						if (supplyCost <= gameService.getGame().getCurrentPlayerPosition().getPrestige()) {
							buyB.setEnabled(true);
						} else {
							buyB.setEnabled(false);
							totalCostTF.setText("You do not have enough prestige to buy the supplies.");
						}
					} else {
						buyB.setEnabled(false);
						totalCostTF.setText(validator.getMessage());
					}
				}

			});
			panel.add(supplyBuyTFs[i - 1]);
			i++;
		}
		panel.addLabel("Total Cost", GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * (i + 1), GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		totalCostTF.setEditable(false);
		totalCostTF.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * (i + 1), GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
		panel.add(totalCostTF);
		buyB = new JButton("Buy and place");
		buyB.setBounds((panel.getWidth() - GraphicTest.COLUMN_WIDTH_LARGE) / 2, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (i + 3), GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		buyB.setEnabled(false);
		buyB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Stock stock = new Stock();
				for (int i = 0; i < supplyBuyTFs.length; i++) {
					stock.put(SupplyType.values()[i], Double.parseDouble(supplyBuyTFs[i].getText()));
				}
				managementService.buySupplies(stock, gameService.getGame().getCurrentPlayerPosition(), location);
				parent.updateSuppliesDisplay(stock, location);
				dispose();
				setVisible(false);
			}

		});
		panel.add(buyB);
		add(panel);
	}

}
