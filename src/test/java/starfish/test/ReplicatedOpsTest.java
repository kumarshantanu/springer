package starfish.test;

import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import starfish.GenericOpsWrite;
import starfish.IOpsRead;
import starfish.IOpsWrite;
import starfish.ReplicatedOpsRead;
import starfish.ReplicationSlavesPointer;
import starfish.test.helper.OpsTestBatch;
import starfish.test.helper.OpsTestSingle;
import starfish.test.helper.TestUtil;

public class ReplicatedOpsTest {

    private static DataSource dataSource;
    private static OpsTestSingle opsTestSingle;
    private static OpsTestBatch  opsTestBatch;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        dataSource = TestUtil.makeTestDataSource();
        opsTestSingle = new OpsTestSingle(dataSource);
        opsTestBatch = new OpsTestBatch(dataSource);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        opsTestSingle = null;
        opsTestBatch = null;
    }

    final IOpsWrite<Integer, String> writer = new GenericOpsWrite<Integer, String>(TestUtil.meta);
    final List<DataSource> slaveDataSources = TestUtil.makeSlaveTestDataSources();
    final IOpsRead<Integer, String> reader = new ReplicatedOpsRead<Integer, String>(TestUtil.meta, Integer.class, String.class, new ReplicationSlavesPointer() {
        public List<DataSource> getDataSources() {
            return slaveDataSources;
        }
    });

    @Before
    public void setUp() throws Exception {
        TestUtil.createTable(dataSource);
    }

    @After
    public void tearDown() throws Exception {
        TestUtil.dropTable(dataSource);
    }

    @Test
    public void replicatedCrudTest() {
        opsTestSingle.crudTest(writer, reader);
    }

    @Test
    public void replicatedBatchCrudTest() {
        opsTestBatch.crudTest(writer, reader);
    }

    @Test
    public void replicatedVersionTest() {
        opsTestSingle.versionTest(writer, reader);
    }

    @Test
    public void replicatedBatchVersionTest() {
        opsTestBatch.versionTest(writer, reader);
    }

    @Test
    public void replicatedReadTest() {
        opsTestSingle.readTest(writer, reader);
    }

    @Test
    public void replicatedBatchReadTest() {
        opsTestBatch.readTest(writer, reader);
    }

}
