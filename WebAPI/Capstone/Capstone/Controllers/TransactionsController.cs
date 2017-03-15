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
                 var id = transactionApi.CreateTransaction(model);
                return Json(new
                {
                    result = id,
                    success = true,
                });
            }
            catch(Exception ex)
            {
                return Json(new
                {
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
                return Json(new
                {
                    success = false,
                });
            }
        }
    }
}
