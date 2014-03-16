package starfish;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface JdbcRead {

    public long NO_LIMIT = -1;
    public boolean THROW_LIMIT_EXCEED_EXCEPTION = true, NO_LIMIT_EXCEED_EXCEPTION = false;

    public List<Map<String, Object>> queryForList(Connection conn, String sql, Object[] args);

    public List<Map<String, Object>> queryForList(Connection conn, String sql, Object[] args,
            long limit, boolean throwLimitExceedException);

    public <T> List<T> queryForList(Connection conn, String sql, Object[] args, RowExtractor<T> extractor);

    public <T> List<T> queryForList(Connection conn, String sql, Object[] args, RowExtractor<T> extractor,
            long limit, boolean throwLimitExceedException);

    public <K, V> Map<K, V> queryForMap(Connection conn, String sql, Object[] args, RowExtractor<K> keyExtractor,
            RowExtractor<V> valueExtractor);

    public <K, V> Map<K, V> queryForMap(Connection conn, String sql, Object[] args, RowExtractor<K> keyExtractor,
            RowExtractor<V> valueExtractor, long limit, boolean throwLimitExceedException);

    public <T> T queryCustom(Connection conn, String sql, Object[] args, ResultSetExtractor<T> extractor);

}
