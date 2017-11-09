package com.niedzwiecki.przemyslguide;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;
import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.data.local.DatabaseHelper;
import com.niedzwiecki.przemyslguide.data.local.PreferencesHelper;
import com.niedzwiecki.przemyslguide.data.model.Ribot;
import com.niedzwiecki.przemyslguide.data.remote.GuideService;
import com.niedzwiecki.przemyslguide.test.common.TestDataFactory;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test class performs local unit tests without dependencies on the Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g. RetrofitServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock DatabaseHelper mMockDatabaseHelper;
    @Mock PreferencesHelper mMockPreferencesHelper;
    @Mock
    GuideService mMockGuideService;
    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockGuideService, mMockPreferencesHelper,
                mMockDatabaseHelper, stringManager, resourcesManager);
    }

    @Test
    public void syncRibotsEmitsValues() {
        List<Ribot> ribots = Arrays.asList(TestDataFactory.makeRibot("r1"),
                TestDataFactory.makeRibot("r2"));
        stubSyncRibotsHelperCalls(ribots);

        TestSubscriber<Ribot> result = new TestSubscriber<>();
        mDataManager.syncRibots().subscribe(result);
        result.assertNoErrors();
        result.assertReceivedOnNext(ribots);
    }

    @Test
    public void syncRibotsCallsApiAndDatabase() {
        List<Ribot> ribots = Arrays.asList(TestDataFactory.makeRibot("r1"),
                TestDataFactory.makeRibot("r2"));
        stubSyncRibotsHelperCalls(ribots);

        mDataManager.syncRibots().subscribe();
        // Verify right calls to helper methods
        verify(mMockGuideService).getPlaces();
        verify(mMockDatabaseHelper).setRibots(ribots);
    }

    @Test
    public void syncRibotsDoesNotCallDatabaseWhenApiFails() {
        when(mMockGuideService.getPlaces())
                .thenReturn(Observable.<List<Ribot>>error(new RuntimeException()));

        mDataManager.syncRibots().subscribe(new TestSubscriber<Ribot>());
        // Verify right calls to helper methods
        verify(mMockGuideService).getPlaces();
        verify(mMockDatabaseHelper, never()).setRibots(ArgumentMatchers.<Ribot>anyList());
    }

    private void stubSyncRibotsHelperCalls(List<Ribot> ribots) {
        // Stub calls to the ribot service and database helper.
        when(mMockGuideService.getPlaces())
                .thenReturn(Observable.just(ribots));
        when(mMockDatabaseHelper.setRibots(ribots))
                .thenReturn(Observable.from(ribots));
    }

}
