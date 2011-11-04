package org.jboss.as.console.client.shared.runtime.tx;

import com.gwtplatform.mvp.client.View;
import org.jboss.as.console.client.domain.model.Server;
import org.jboss.as.console.client.shared.runtime.Metric;

import java.util.List;

/**
 * @author Heiko Braun
 * @date 11/3/11
 */
public interface TXMetricView extends View {
    void setPresenter(TXMetricManagement presenter);
    void setTxMetric(Metric txMetric);
    void setRollbackMetric(Metric rollbackMetric);
    void setServer(List<Server> server);
    void recycleCharts();
}