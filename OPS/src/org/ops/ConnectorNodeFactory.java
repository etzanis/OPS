package org.ops;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MyOps" Node.
 * 
 *
 * @author 
 */
public class ConnectorNodeFactory 
        extends NodeFactory<ConnectorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectorNodeModel createNodeModel() {
        return new ConnectorNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<ConnectorNodeModel> createNodeView(final int viewIndex,
            final ConnectorNodeModel nodeModel) {
        return new ConnectorNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new ConnectorNodeDialog();
    }

}

