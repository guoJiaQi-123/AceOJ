import type {
  BaseResponse_boolean_,
  BaseResponse_long_,
  BaseResponse_Page_PostVO_,
  BaseResponse_PostVO_,
  CancelablePromise,
  DeleteRequest,
  PostAddRequest,
  PostEditRequest,
  PostQueryRequest,
  PostUpdateRequest,
} from "@/generated";
import { OpenAPI } from "@/generated";
import { request as __request } from "../core/request";

export class PostControllerService {
  /**
   * addPost
   * @param postAddRequest postAddRequest
   * @returns BaseResponse_long_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static addPostUsingPost(
    postAddRequest: PostAddRequest
  ): CancelablePromise<BaseResponse_long_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/post/add",
      body: postAddRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * deletePost
   * @param deleteRequest deleteRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static deletePostUsingPost(
    deleteRequest: DeleteRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/post/delete",
      body: deleteRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * editPost
   * @param postEditRequest postEditRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static editPostUsingPost(
    postEditRequest: PostEditRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/post/edit",
      body: postEditRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getPostVOById
   * @param id id
   * @returns BaseResponse_PostVO_ OK
   * @throws ApiError
   */
  public static getPostVoByIdUsingGet(
    id?: number
  ): CancelablePromise<BaseResponse_PostVO_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/post/get/vo",
      query: {
        id: id,
      },
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * listPostVOByPage
   * @param postQueryRequest postQueryRequest
   * @returns BaseResponse_Page_PostVO_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static listPostVoByPageUsingPost(
    postQueryRequest: PostQueryRequest
  ): CancelablePromise<BaseResponse_Page_PostVO_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/post/list/page/vo",
      body: postQueryRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * listMyPostVOByPage
   * @param postQueryRequest postQueryRequest
   * @returns BaseResponse_Page_PostVO_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static listMyPostVoByPageUsingPost(
    postQueryRequest: PostQueryRequest
  ): CancelablePromise<BaseResponse_Page_PostVO_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/post/my/list/page/vo",
      body: postQueryRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * searchPostVOByPage
   * @param postQueryRequest postQueryRequest
   * @returns BaseResponse_Page_PostVO_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static searchPostVoByPageUsingPost(
    postQueryRequest: PostQueryRequest
  ): CancelablePromise<BaseResponse_Page_PostVO_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/post/search/page/vo",
      body: postQueryRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * updatePost
   * @param postUpdateRequest postUpdateRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static updatePostUsingPost(
    postUpdateRequest: PostUpdateRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/post/update",
      body: postUpdateRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
