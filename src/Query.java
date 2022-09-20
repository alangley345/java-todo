public abstract class Query<T> implements Runnable {
	
	public abstract T getTasks();
		
	
	@Override
	public void run() {
		getTasks();
	}


}
