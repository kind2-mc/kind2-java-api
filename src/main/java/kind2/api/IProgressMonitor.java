package kind2.api;

public interface IProgressMonitor {
	public boolean isCanceled();

	public void done();
}