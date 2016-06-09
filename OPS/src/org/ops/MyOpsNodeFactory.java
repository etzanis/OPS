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
public class MyOpsNodeFactory 
        extends NodeFactory<MyOpsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MyOpsNodeModel createNodeModel() {
        return new MyOpsNodeModel();
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
    public NodeView<MyOpsNodeModel> createNodeView(final int viewIndex,
            final MyOpsNodeModel nodeModel) {
        return new MyOpsNodeView(nodeModel);
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
        return new MyOpsNodeDialog();
    }

}

