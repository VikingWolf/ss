package org.orion.ss.test.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.orion.ss.model.Unit;
import org.orion.ss.orders.Order;
import org.orion.ss.service.GameService;
import org.orion.ss.service.OrderService;
import org.orion.ss.service.ServiceFactory;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.test.components.FastPanel;
import org.orion.ss.test.components.LocationUpdatable;
import org.orion.ss.test.order.OrderExecutor;
import org.orion.ss.test.order.OrderPanel;
import org.orion.ss.test.order.OrderPanelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitOrdersDialog extends JDialog implements OrderExecutor {

	private static final long serialVersionUID = -3709849519497626262L;
	protected final static Logger logger = LoggerFactory.getLogger(UnitOrdersDialog.class);

	private final GameService gameService;
	private final OrderService orderService;

	private final OrderPanelFactory orderPanelFactory;
	private OrderPanel<?, ?> orderP;
	private final JTextField resultTF;
	private JLabel timeL;

	private FastPanel panel;
	private JButton executeB;
	private final LocationUpdatable locationUpdatable;

	private final Unit unit;
	private Order<?> order = null;

	public UnitOrdersDialog(GameService gameService, Unit unit, JTextField resultTF, LocationUpdatable locationUpdatable) {
		super();
		this.gameService = gameService;
		this.unit = unit;
		this.resultTF = resultTF;
		this.locationUpdatable = locationUpdatable;
		orderService = ServiceFactory.getOrderService(this.gameService.getGame());
		orderPanelFactory = new OrderPanelFactory(unit, this);
		setTitle("Orders for unit " + unit.getFullLongName());
		setBounds((int) GraphicTest.DEFAULT_DIALOG_LOCATION.getX(), (int) GraphicTest.DEFAULT_DIALOG_LOCATION.getY(), 600, 400);
		orderPanelFactory.setPanelsBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT * 2,
				this.getWidth() - GraphicTest.LEFT_MARGIN - GraphicTest.RIGHT_MARGIN - GraphicTest.LATERAL_SWING_MARGIN,
				280);
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
		mount();
		setVisible(true);
	}

	protected void mount() {
		panel = new FastPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, getWidth(), getHeight());
		panel.addLabel("Select an action and follow the instructions",
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_XXXLARGE,
				GraphicTest.ROW_HEIGHT
				);
		final JComboBox<Order<?>> ordersCB = new JComboBox<Order<?>>(orderService.buildOrders(unit).toArray(new Order<?>[] {}));
		ordersCB.setBounds(GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH_XXLARGE,
				GraphicTest.ROW_HEIGHT);
		panel.add(ordersCB);

		timeL = new JLabel();
		timeL.setText("Time=");
		timeL.setBounds(
				ordersCB.getX() + ordersCB.getWidth() + GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		panel.add(timeL);
		executeB = new JButton("Execute");
		executeB.setBounds(
				timeL.getX() + timeL.getWidth() + GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH_LARGE,
				GraphicTest.ROW_HEIGHT);
		panel.add(executeB);
		executeB.setEnabled(false);
		add(panel);
		ordersCB.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					setOrder((Order<?>) ordersCB.getSelectedItem());
					mountOrderPanel();
				}
			}

		});
		executeB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				getResultTF().setText(orderService.execute(getOrder()));
				locationUpdatable.refreshLocation();
				dispose();
				setVisible(false);
			}

		});
		setOrder((Order<?>) ordersCB.getSelectedItem());
		mountOrderPanel();
	}

	protected void mountOrderPanel() {
		if (orderP != null)
			panel.remove(panel.getComponentCount() - 1);
		orderP = orderPanelFactory.getOrderPanel(getOrder());
		panel.add(orderP);
		panel.repaint();
	}

	public Unit getUnit() {
		return unit;
	}

	/* getters & setters */

	@Override
	public void updateExecution(boolean canExecute) {
		executeB.setEnabled(canExecute);
	}

	public Order<?> getOrder() {
		return order;
	}

	public void setOrder(Order<?> order) {
		this.order = order;
		timeL.setText("Time=" + order.getOrderTime().getDenomination());
	}

	public JTextField getResultTF() {
		return resultTF;
	}

}