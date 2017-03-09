using Capstone.Sdk;
using Microsoft.AspNet.Identity.Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using System.Web.Security;

namespace Capstone.Controllers
{
    public class TransactionsController : ApiController
    {
        [HttpPost]
        [Route("api/Transactions/GetTransactionByUsername")]
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
