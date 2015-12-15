package cn.academy.buff;

public class Combiner {
	ILevelCombiner levelCombiner;
	IDurationCombiner durationCombiner;
	
	public Combiner(ILevelCombiner levelCombiner,IDurationCombiner durationCombiner) {
		this.levelCombiner = levelCombiner;
		this.durationCombiner = durationCombiner;
	}
	
	public void combine(Buff baseBuff,Buff combinedBuff) {
		baseBuff.isDurationForever |= combinedBuff.isDurationForever;
		baseBuff.duration = durationCombiner.combineDuration(baseBuff, combinedBuff);
		baseBuff.level = Math.min(
				levelCombiner.combineLevel(baseBuff, combinedBuff), 
				baseBuff.getType().getMaxLevel()
				);
	}
	
	public interface ILevelCombiner {
		public int combineLevel(Buff baseBuff,Buff combinedBuff);
		
		public static ILevelCombiner Sum = new ILevelCombiner() {
			@Override
			public int combineLevel(Buff baseBuff, Buff combinedBuff) {
				return baseBuff.level + combinedBuff.level;
			}
		};
		
		public static ILevelCombiner Max = new ILevelCombiner() {
			@Override
			public int combineLevel(Buff baseBuff, Buff combinedBuff) {
				return Math.max(baseBuff.level, combinedBuff.level);
			}
		};
		
		public static ILevelCombiner PlusOne = new ILevelCombiner() {
			@Override
			public int combineLevel(Buff baseBuff, Buff combinedBuff) {
				return baseBuff.level+1;
			}
		};
	}
	
	public interface IDurationCombiner {
		public int combineDuration(Buff baseBuff,Buff combinedBuff);
		
		public static IDurationCombiner Sum = new IDurationCombiner() {
			@Override
			public int combineDuration(Buff baseBuff, Buff combinedBuff) {
				return baseBuff.duration + combinedBuff.duration;
			}
		};
		
		public static IDurationCombiner Max = new IDurationCombiner() {
			@Override
			public int combineDuration(Buff baseBuff, Buff combinedBuff) {
				return Math.max(baseBuff.duration, combinedBuff.duration);
			}
		};
	}
}
