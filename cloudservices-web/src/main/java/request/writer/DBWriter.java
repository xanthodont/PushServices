package request.writer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import utils.ApplicationContextUtil;
import utils.StringUtil;



public abstract class DBWriter<T> {
	private static Logger logger = Logger.getLogger(DBWriter.class);
	/** 数据库操作语句 */
	protected final String sqlStatement;
	/** 数据库连接池 */
	protected DataSource dataSourcePool;
	/** 消息队列 */
	protected final BlockingQueue<T> queue;
	/** 写入数据库线程 */
	protected Thread writerThread;
	protected ExecutorService writerThreadpool;
	/** 线程开关，线程是否完成 */
    volatile boolean done;
    /** 线程名称 */
    protected String threadName;
    private int threadPoolSize = InitConfiguration.getInstance().getCacheQueueThreadSize();
    private int bantchSize = 2000;
	
	public DBWriter(String sqlStatement) {
		dataSourcePool = (DataSource) ApplicationContextUtil.getBeanByName("dataSource");
		this.sqlStatement = sqlStatement;
		this.queue = new ArrayBlockingQueue<T>(InitConfiguration.getInstance().getCacheQueueSize());
		init();
	}	
	
	private void init() {
		done = false;
		writerThreadpool = Executors.newFixedThreadPool(threadPoolSize);
		writerThread = new Thread() {
			public void run() {
				// TODO Auto-generated method stub
				writeToDatabase(this);
			}
		};
		if (!StringUtil.isEmpty(threadName)) writerThread.setName(threadName);
		writerThread.setDaemon(true);
	} 
	
	/**
	 * 写入数据库
	 * @param thisThread
	 */
	public void writeToDatabase(Thread thisThread) {
		int count = 0;
		Connection jdbcConn = null;
		PreparedStatement ps = null;
		try {
			while(!done && (writerThread == thisThread)) {
				T info = nextInfo();

				if (count == 0 && !StringUtil.isEmpty(sqlStatement)) {
					jdbcConn = dataSourcePool.getConnection();
					ps = jdbcConn.prepareStatement(sqlStatement);
				}
					
				try {
					if (info != null) {
						setPrepareStatement(ps, info);
						count++;
					} 
				} catch (Exception e) {
					e.printStackTrace();
					logger.debug("json parse error!! Params:");
					continue;
				} finally {
					if (count > bantchSize || queue.isEmpty()) {
						//logger.debug("count:" + count + " ps:");
						if (ps != null) {
							logger.debug("execute Batch count:"+count);
							try {
								ps.executeBatch();
							} catch (Exception e) {
								logger.error("execute Batch Exception");
							} 
						}
						count = 0;
						colsePreparedStatement(ps);
						closeConnection(jdbcConn);
					}
					info = null; // 加快GC回收
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.printf("appWriter exception!!");
			e.printStackTrace();
		} finally {
			closeConnection(jdbcConn);
		}
	}

	/**
	 * 配置预处理参数
	 * @param ps PrepareStatement预处理
	 * @param info info数据
	 * @param json params参数的Json对象
	 * @throws SQLException
	 */
	protected abstract void setPrepareStatement(PreparedStatement ps, T info) throws SQLException;

	
	public void putInfo(T info) {
		// TODO Auto-generated method stub
		if (!done) {
			try {
				queue.put(info);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				return;
			}
			synchronized (queue) {
				queue.notifyAll();
			}
		}
	}

	
	private T nextInfo() {
		// TODO Auto-generated method stub
		T info = null;
        while (!done && (info = queue.poll()) == null) {
            try {
                synchronized (queue) {
                    queue.wait();
                }
            }
            catch (InterruptedException ie) {
                // Do nothing
            }
        }
        return info;
	}
	
	public void startup() {
		/** 并行执行 */
		for (int i = 0; i < threadPoolSize; i++) {
			writerThreadpool.execute(writerThread);
		}
		/** 单线程效率低 */
		//writerThread.start();
	}
	
	public void shutdown() {
		done = true;
        synchronized (queue) {
            queue.notifyAll();
        }
	}
	
	protected void closeConnection(Connection jdbcConn) {
		if (jdbcConn != null) {
			try {
				jdbcConn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void colsePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
