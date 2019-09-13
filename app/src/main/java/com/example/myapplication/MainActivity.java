package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplication.api.RetrofitAPIService;
import com.example.myapplication.api.RetrofitClientFactory;
import com.example.myapplication.model.Entry;
import com.example.myapplication.model.Quotes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.hiTxt)
    TextView hiTxt;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    //CustomAdapter customAdapter;

    QuotesAdapter quotesAdapter;
    RetrofitAPIService retrofitAPIService;
    LinearLayoutManager linearLayoutManager;

    private final String TAG = "RxJava";

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Retrofit retrofit = RetrofitClientFactory.getInstance();
        retrofitAPIService = retrofit.create(RetrofitAPIService.class);
        compositeDisposable = new CompositeDisposable();
       //  composite disposables is a list of Observables
        /*compositeDisposable.add(retrofitAPIService.emittedItems()
                // using subscribeOn since we don't want to block the main thread
                              .subscribeOn(Schedulers.io())
                // after fetching the data from the background the UI has to updated on the main thread
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(this::displayListofQuotes));
*/

        // Example in case we need to update the UI after every few intervals
        Observable.interval(0,3,TimeUnit.SECONDS)
                .flatMap(i->retrofitAPIService.emittedItems()
                .subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error-> new ArrayList<Quotes>())
                .subscribe(this::displayListofQuotes);


      //  customAdapter = new CustomAdapter();
      //  recyclerView.setAdapter(customAdapter);
        // We use this method to specify a data source
        Observable.just("This is a demo app").subscribe(s -> {
            Log.d("text",s);
            hiTxt.setText(s);
        }, throwable -> {

        });

/*
        Observable.just("Delhi","Mumbai","Pune","Bangalore","Chennai","Kolkatta").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                customAdapter.addStringToList(s);
            }
        });
*/

        /*Entry entry1 = new Entry("Delhi", BigDecimal.valueOf(1800),new Date());
        Entry entry2 = new Entry("Mumbai", BigDecimal.valueOf(2000),new Date());
        Entry entry3 = new Entry("Pune", BigDecimal.valueOf(1700),new Date());
        Entry entry4 = new Entry("Bangalore", BigDecimal.valueOf(1800),new Date());

        Observable.just(entry1,entry2,entry3,entry4).subscribe(new Consumer<Entry>() {
            @Override
            public void accept(Entry entry) throws Exception {
                 customAdapter.addEntry(entry);
            }
        });*/


        Observable.just("Rakhi, Pune").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("value",s);
            }
        });

        /*
        * A Disposable controls the lifecycle of an observable
        * If the data stream produced by an observable is limitless, the observable will be active forever
        * In short a disposable is used to control an observables*/
        // The subscriber return type is a disposable
        Disposable disposable = Observable.just("Disposable Demo").subscribe();
        disposable.dispose();
        if(disposable.isDisposed())
        {

        }
        else
        {

        }

        // Creating a group of disposables
        Disposable groupOfDisposables = new CompositeDisposable(
                Observable.just("hi").subscribe(),
                Observable.just("How").subscribe(),
                Observable.just("are").subscribe(),
                Observable.just("you").subscribe()
        );

        groupOfDisposables.dispose();

        // setting a setting scheduler with subscribeOn on the observable
        // A scheduler specifies a unit of work to be executed now or later
        // A scheduler can select a thread to execute code. In this way the UI thread won't be blocked or frozen
        // We use schedulers to execute heavy long tasks in the background, so that it won't block th main UI thread

        Observable.just("This is some sample data","Its fun learning RxJava").subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                // getting the thread on which the accept method gets executed
                Log.d(TAG,"The accept method is executed on "+Thread.currentThread().getName()+ " "+ s);
            }
        });

        Observable.just("just another observable with a lambda function","Checking state and value")
                  .doOnNext(value->showcurrentThreadNameOnTheLogWithStateAndValue("doOnNext",value))
                  .subscribe(sValue->showcurrentThreadNameOnTheLogWithStateAndValue("subscribe",sValue));

        Observable.just("Value One","Value Two")
                .subscribeOn(Schedulers.io())
                .doOnNext(value->showcurrentThreadNameOnTheLogWithStateAndValue("doOnNext",value))
                .subscribe(sValue->showcurrentThreadNameOnTheLogWithStateAndValue("subscribe",sValue));

        // Condition where you want to do some network operation in the background thread and then switch back to the main thread
         Observable.just("Some internet operation")
                   .subscribeOn(Schedulers.io())
                   .doOnNext(value->showcurrentThreadNameOnTheLogWithStateAndValue("doOnNext",value))
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(sValue->showcurrentThreadNameOnTheLogWithStateAndValue("subscribe",sValue));

       // Example of Observable Flow
        Observable.just("Mumbai","Pune")
                .subscribeOn(Schedulers.io())
                .doOnNext(i->showcurrentThreadNameOnTheLogWithStateAndValue("doOnNext",i))
                .doOnEach(i->showcurrentThreadNameOnTheLogWithStateAndValue("doOnEach", String.valueOf(i)))
                .doOnComplete(()->showcurrentThreadNameOnTheLogWithState("doOnComplete"))
                .doOnTerminate(()->showcurrentThreadNameOnTheLogWithState("doOnTerminate"))
                .doFinally(()->showcurrentThreadNameOnTheLogWithState("doFinally"))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(i->showcurrentThreadNameOnTheLogWithState("doOnSubscribe"))
                .subscribe(i->showcurrentThreadNameOnTheLogWithStateAndValue("subscribe",i));



        // Example of implementing Flowables in RxJava
        PublishSubject<Integer> myObservable = PublishSubject.create();
        // defining different stategy types and the way each strategy differs

        // Example 1 : Bad Strategy = No Strategy
/*
        myObservable.observeOn(Schedulers.computation())
                     .subscribe(item->showcurrentThreadNameOnTheLogWithStateAndValue("subscribe", String.valueOf(item)));

        for(int i=0;i<2000000;i++)
        {
            myObservable.onNext(i);
        }
*/

        // Example 2 : Missing Exception
        // Will throw an excepton Caused by: io.reactivex.exceptions.MissingBackpressureException: Queue is full?!
        // By converting the Observable to flowable we can control the large amount of data emitted by it
     /*   myObservable.toFlowable(BackpressureStrategy.MISSING)
                .observeOn(Schedulers.computation())
                .subscribe(item->showcurrentThreadNameOnTheLogWithStateAndValue("subscribe", String.valueOf(item)));

        for(int i=0;i<140;i++)
        {
            myObservable.onNext(i);
        }*/

     // Example 3 : Dropping Strategy

       /* myObservable.toFlowable(BackpressureStrategy.DROP)
                .observeOn(Schedulers.computation())
                .subscribe(item->showcurrentThreadNameOnTheLogWithStateAndValue("subscribe", String.valueOf(item)));

        for(int i=0;i<2000000;i++)
        {
            myObservable.onNext(i);
        }*/

       // Example 4 : Sample stategy

  /*      myObservable.toFlowable(BackpressureStrategy.MISSING)
                .sample(10, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .subscribe(item->showcurrentThreadNameOnTheLogWithStateAndValue("subscribe", String.valueOf(item)));

  */

      // Example 5 : Latest Strategy

        /*myObservable.toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.computation())
                .subscribe(item->showcurrentThreadNameOnTheLogWithStateAndValue("subscribe", String.valueOf(item)));
*/

      // Example 6 : Debounce Strategy

        myObservable.toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.computation())
                .debounce(10,TimeUnit.MILLISECONDS)
                .subscribe(item->showcurrentThreadNameOnTheLogWithStateAndValue("subscribe", String.valueOf(item)));

      // Example 7 : Buffer Strategy
      // Will Buffer items until it can consume the next items, chances of it throwing Out Of Memory Exception, incase it cannot consume
       /* myObservable.toFlowable(BackpressureStrategy.BUFFER)
                .observeOn(Schedulers.computation())
                .subscribe(item->showcurrentThreadNameOnTheLogWithStateAndValue("subscribe", String.valueOf(item)));
*/

        for(int i=0;i<2000000;i++)
        {
            myObservable.onNext(i);
        }

        // Example of RxJava Completables

        Completable completable = Completable.fromAction(()->{
            Log.d(TAG,"Joey wants to learn German in 3 months");
            showcurrentThreadNameOnTheLogWithState("fromAction");
        });

        // handle the future action inside the subscribe method of the completable
        completable.subscribe(()->{
           Log.d(TAG,"Handle the action after its complete");
           Log.d(TAG,"Joey has now learnt German");
            showcurrentThreadNameOnTheLogWithState("subscribe");
        }, throwable -> {
            // In case the action cannot be completed due to certain reasons throwable will be executed
            Log.d(TAG,throwable.toString());
        });

        // Example of using Single in RxJava
        // Single emits a single item instead of a stream of items
        // Helpful in cases when you want to handle a single unique value

        Single single = Single.just("King of the jungle");
        single.subscribe(kingOfTheJungle -> {
            Log.d(TAG,"Lion is the "+kingOfTheJungle);
        },throwable -> {
            Log.d(TAG,throwable.toString());
        });

        // Example of MayBe in RxJava, which  is a combination of completable & single
        Maybe.just("Joey want to become an iOS developer ")
                .subscribe(success->Log.d(TAG,"Ready ! Let's do it !"),
                        throwable -> Log.d(TAG,"Joey failed to become an iOS developer"),
                        // onComplete result
                        ()->Log.d(TAG,"Congrats Joe ! You're now an iOS developer"));


    }

    private void showcurrentThreadNameOnTheLogWithStateAndValue(String state,String value)
    {
        String description = Thread.currentThread().getName() + " Thread "+ " - "+ " State "+ state + " - "+ " Value "+ value;
        Log.d(TAG,description);
    }

    private void showcurrentThreadNameOnTheLogWithState(String state)
    {
        String description = Thread.currentThread().getName() + " Thread "+ " - "+ " State "+ state ;
        Log.d(TAG,description);
    }

    private void displayListofQuotes(List<Quotes> quotesList)
    {
        quotesAdapter = new QuotesAdapter(MainActivity.this,quotesList);
        recyclerView.setAdapter(quotesAdapter);
    }

}
