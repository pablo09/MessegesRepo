/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Pawel
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/NewMessage")
})
public class NewMessage implements MessageListener {
    @Resource
    private MessageDrivenContext mdc;
    @PersistenceContext(unitName="jsf_EJB_test-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;
    public NewMessage() {
    }
    
   public void onMessage(Message message) {
     ObjectMessage msg = null;
     try {
          if (message instanceof ObjectMessage) {
          msg = (ObjectMessage) message;
              NewsEntity e = (NewsEntity) msg.getObject();
              save(e);
          }
     } catch (JMSException e) {
          e.printStackTrace();
          mdc.setRollbackOnly();
     } catch (Throwable te) {
          te.printStackTrace();
     }
}
    private void save(Object object) {
        em.persist(object);
    }
}
