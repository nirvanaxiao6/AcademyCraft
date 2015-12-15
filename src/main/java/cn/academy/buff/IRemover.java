package cn.academy.buff;

public interface IRemover {
	boolean remove(Buff buff);
	
	public static IRemover RemoveOneLevel = new IRemover() {
		@Override
		public boolean remove(Buff buff) {
			buff.level--;
			buff.duration = buff.lastDuration;
			return buff.level>0;
		}
	};
	
	public static IRemover RemoveAll = new IRemover() {
		
		@Override
		public boolean remove(Buff buff) {
			return true;
		}
	};
}
