using Capstone.Models;
using Capstone.Sdk;
using Capstone.ViewModels;
using Microsoft.AspNet.Identity.Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using System.Web.Http.Description;
using System.Web.Security;

namespace Capstone.Controllers
{
    [RoutePrefix("api/Transactions")]
    public class TransactionsController : ApiController
    {
        [HttpPost]
        [Route("CreateTransaction")]
        public async Task<IHttpActionResult> CreateTransaction(TransactionCreateViewModel model)
        {
            try
            {
                var transactionApi = new TransactionApi();
                var UserManager = Request.GetOwinContext().GetUserManager<ApplicationUserManager>();
                var user = await UserManager.FindByNameAsync(model.Username);
                var userId = user.Id;
                model.TransactionDate = DateTime.Now;
                model.AspNetUserId = userId;
                model.EndTime = model.TransactionDate.AddHours(model.Duration);
                var transaction = transactionApi.CreateTransaction(model);
                var parkingLot = (new ParkingLotApi()).Get(model.ParkingLotId);
                transaction.TransactionCode = parkingLot.Address + transaction.Id + "";
                transactionApi.Edit(transaction.Id, transaction);
                TransactionCreateReturnViewModel result = new TransactionCreateReturnViewModel()
                {
                    Id = transaction.Id,
                    Amount = transaction.Amount,
                    AspNetUserId = transaction.AspNetUserId,
                    CarParkId = transaction.CarParkId,
                    EndTime = transaction.EndTime,
                    LongEndTime = transaction.EndTime.Value.Ticks,
                    ParkingLotId = transaction.ParkingLotId,
                    Status = transaction.Status,
                    TransactionCode = transaction.TransactionCode,
                    TransactionDate = transaction.TransactionDate,
                };
                return Json(new ResultModel
                {
                    obj = result,
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new ResultModel
                {
                    success = false,
                });
            }
        }

        [HttpPost]
        [Route("UpdateTransaction")]
        public IHttpActionResult UpdateTransaction(TransactionUpdateViewModel model)
        {
            try
            {
                var transactionApi = new TransactionApi();
                transactionApi.ChangeStatus(model);
                return Json(new ResultModel
                {
                    message = "Đổi trạng thái thành công!",
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new ResultModel
                {
                    message = "Có lỗi xảy ra, vui lòng liên hệ admin",
                    success = false,
                });
            }
        }

        [HttpPost]
        [Route("GetTransactionByUsername")]
        [ResponseType(typeof(List<TransactionCustomViewModel>))]
        public async Task<IHttpActionResult> GetTransactionByUsername(string username)
        {
            try
            {
                var transactionApi = new TransactionApi();
                var UserManager = Request.GetOwinContext().GetUserManager<ApplicationUserManager>();
                var user = await UserManager.FindByNameAsync(username);
                var userId = user.Id;
                var listTransaction = transactionApi.GetTransactionByUserId(userId);
                return Json(new
                {
                    result = listTransaction,
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new ResultModel
                {
                    success = false,
                });
            }
        }

        [HttpPost]
        [Route("CheckCode")]
        public async Task<IHttpActionResult> CheckCode(CheckCodeModel model)
        {
            try
            {
                var transactionApi = new TransactionApi();
                var UserManager = Request.GetOwinContext().GetUserManager<ApplicationUserManager>();
                var user = await UserManager.FindByNameAsync(model.Username);
                var userId = user.Id;
                var result = transactionApi.CheckCode(userId, model.TransactionCode, model.CarParkId);
                if (result != null)
                {
                    return Json(new ResultModel
                    {
                        obj = result,
                        success = true,
                    });
                }
                else
                {
                    return Json(new ResultModel
                    {
                        message = "Mã hoặc tên tài khoản sai",
                        success = false,
                    });
                }
            }
            catch (Exception ex)
            {
                return Json(new ResultModel
                {
                    message = "Có lỗi xảy ra, vui lòng liên hệ Admin!",
                    success = false,
                });
            }
        }

        public class CheckCodeModel
        {
            public string TransactionCode { get; set; }
            public string Username { get; set; }
            public int CarParkId { get; set; }
        }
    }
}
