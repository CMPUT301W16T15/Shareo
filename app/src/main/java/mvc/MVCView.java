package mvc;

/**
 * Created by A on 2016-02-10.
 */
public interface MVCView<M extends MVCModel> {
    public void updateView(M model);
}
