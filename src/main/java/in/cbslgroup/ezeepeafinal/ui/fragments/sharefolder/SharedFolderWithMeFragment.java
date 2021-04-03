package in.cbslgroup.ezeepeafinal.ui.fragments.sharefolder;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.adapters.list.SharedFolderAdapter;
import in.cbslgroup.ezeepeafinal.model.response.sharedfolder.SharedFolderListItem;
import in.cbslgroup.ezeepeafinal.model.response.sharedfolder.SharedFolderResponse;
import in.cbslgroup.ezeepeafinal.network.RetrofitSingelton;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SharedFolderWithMeFragment extends Fragment {

    RecyclerView rvMain;
    List<SharedFolderListItem> sharedFolderListItemList = new ArrayList<>();
    TextView tvNothingFound;
    ProgressBar pbMain;
    SharedFolderAdapter sharedFolderAdapter;

    private void initLocale() {
        String lang = LocaleHelper.getPersistedData(getActivity(), null);
        if (lang == null) {

            LocaleHelper.persist(getActivity(), "en");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(LocaleHelper.onAttach(context, "en"));
    }

    public SharedFolderWithMeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shared_folder_with_me, container, false);
        initLocale();
        initViews(v);
        initRecyclerViews(v);

         getSharedFolderWithMeList(new SessionManager(getActivity()).getUserId());

        return v;

    }


    void initViews(View v){

        tvNothingFound = v.findViewById(R.id.tv_no_shared_folder_found_with_me);
        pbMain = v.findViewById(R.id.pb_share_folder_main_with_me);
    }


    void initRecyclerViews(View v)
    {
        rvMain = v.findViewById(R.id.rv_shared_folder_with_me);
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));


    }


    void getSharedFolderWithMeList(String userid){

        pbMain.setVisibility(View.VISIBLE);
        rvMain.setVisibility(View.GONE);
        tvNothingFound.setVisibility(View.GONE);

        RetrofitSingelton.getClient().getSharedFolderList("getShareFolderWithMeList",userid).enqueue(new Callback<SharedFolderResponse>() {
            @Override
            public void onResponse(Call<SharedFolderResponse> call, Response<SharedFolderResponse> response) {

                if(response!=null){

                    if(response.isSuccessful()){

                        SharedFolderResponse item = response.body();
                        if(item.getError().equalsIgnoreCase("false")){

                            sharedFolderAdapter = new SharedFolderAdapter(item.getList(),getActivity(),"share_folder_with_me");
                            rvMain.setAdapter(sharedFolderAdapter);


                            pbMain.setVisibility(View.GONE);
                            rvMain.setVisibility(View.VISIBLE);
                            tvNothingFound.setVisibility(View.GONE);


                        }

                        else{


                            Toast.makeText(getActivity(),item.getMsg(), Toast.LENGTH_SHORT).show();
                            pbMain.setVisibility(View.GONE);
                            rvMain.setVisibility(View.GONE);
                            tvNothingFound.setVisibility(View.VISIBLE);


                        }


                    }

                    else{

                        Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        pbMain.setVisibility(View.GONE);
                        rvMain.setVisibility(View.GONE);
                        tvNothingFound.setVisibility(View.VISIBLE);
                    }

                }
                else{


                    Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    pbMain.setVisibility(View.GONE);
                    rvMain.setVisibility(View.GONE);
                    tvNothingFound.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<SharedFolderResponse> call, Throwable t) {

                Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                pbMain.setVisibility(View.GONE);
                rvMain.setVisibility(View.GONE);
                tvNothingFound.setVisibility(View.VISIBLE);

            }
        });

    }

}