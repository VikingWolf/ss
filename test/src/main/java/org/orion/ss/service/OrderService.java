package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Unit;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.orders.Attach;
import org.orion.ss.orders.AutoSupply;
import org.orion.ss.orders.Detach;
import org.orion.ss.orders.Garrison;
import org.orion.ss.orders.Order;

public class OrderService extends Service {

	protected OrderService(Game game) {
		super(game);
	}

	public List<Order<?>> buildOrders(Activable activable) {
		List<Order<?>> result = new ArrayList<Order<?>>();
		if (Attach.EXECUTOR_CLASS.isAssignableFrom(activable.getClass())) addOrder(result, new Attach((Unit) activable, getGame()));
		if (Detach.EXECUTOR_CLASS.isAssignableFrom(activable.getClass())) addOrder(result, new Detach((Formation) activable, getGame()));
		if (AutoSupply.EXECUTOR_CLASS.isAssignableFrom(activable.getClass())) addOrder(result, new AutoSupply((Unit) activable, getGame()));
		if (Garrison.EXECUTOR_CLASS.isAssignableFrom(activable.getClass())) addOrder(result, new Garrison((Unit) activable, getGame()));
		return result;
	}

	private void addOrder(List<Order<?>> orders, Order<?> order) {
		if (order.checkRequirements()) orders.add(order);
	}

	public String execute(Order<?> order) {
		String result = order.execute();
		this.getGame().getLog().addEntry(result);
		return result;
	}

}

/* Añadir nueva orden
 * * Servicio
 *  - Crear subclase de Order
 *  - Definir atributo de classe EXECUTOR_CLASS (=tipo generico)
 *  - Implementar los métodos abstractos
 *  - Registrar en OrderService, buildOrders
 *  
 *   * 	Cliente
 * 	- 	Crear subclase de OrderPanel 
 *  -	Añadir la construcción del panel en getOrderPanel en OrderPanelFactory	

 */
