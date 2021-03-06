package com.xiao.factory.net;

import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.model.api.account.AccountResponseModel;
import com.xiao.factory.model.api.account.LoginModel;
import com.xiao.factory.model.api.account.RegisterModel;
import com.xiao.factory.model.api.group.GroupCreateModel;
import com.xiao.factory.model.api.group.GroupMemberAddModel;
import com.xiao.factory.model.api.message.MsgCreateModel;
import com.xiao.factory.model.api.user.UserUpdateModel;
import com.xiao.factory.model.card.GroupCard;
import com.xiao.factory.model.card.GroupMemberCard;
import com.xiao.factory.model.card.MessageCard;
import com.xiao.factory.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RemoteService {

    /**
     * 用户注册请求接口
     */
    @POST("account/register")
    Call<RspModel<AccountResponseModel>> accountRegister(@Body RegisterModel registerModel);

    /**
     * 登录
     */
    @POST("account/login")
    Call<RspModel<AccountResponseModel>> accountLogin(@Body LoginModel loginModel);

    /**
     * 绑定设备ID
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountResponseModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    /**
     * 用户更新
     */
    @PUT("user")
    Call<RspModel<UserCard>> updateUserInfo(@Body UserUpdateModel model);


    /**
     * 搜索用户
     */
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> searchUser(@Path("name") String name);


    /**
     * 用户关注接口
     */
    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> userFollow(@Path("userId") String userId);

    /**
     * 获取联系人列表
     */
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();


    @GET("user/{userId}")
    Call<RspModel<UserCard>> searchUserById(@Path("userId") String userId);

    /**
     * 发送消息的接口
     */
    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);


    /**
     * 创建群
     */
    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel createModel);

    /**
     * 拉取群信息
     */
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);

    /**
     * 群搜索的接口
     */
    @GET("group/search/{name}")
    Call<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);

    /**
     * 我的群列表
     */
    @GET("group/list/{date}")
    Call<RspModel<List<GroupCard>>> groups(@Path(value = "date", encoded = true) String date);


    /**
     * 我的群的成员列表
     */
    @GET("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);


    /**
     * 给群添加成员
     */
    @POST("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId,
                                                         @Body GroupMemberAddModel model);

}
