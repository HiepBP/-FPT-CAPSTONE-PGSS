using Capstone.Sdk;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Capstone.Controllers
{
    public class ItemsController : ApiController
    {
        [HttpPost]
        [Route("api/Items/Create")]
        public IHttpActionResult Create([FromBody]ItemViewModel model)
        {
            ItemApi itemApi = new ItemApi();
            if (!ModelState.IsValid)
            {
                return Json(new
                {
                    success = false,
                });
            }
            try
            {
                itemApi.Create(model);
                return Json(new
                {
                    success = true,
                });
            }
            catch (Exception)
            {
                return Json(new
                {
                    success = false,
                });
            }
        }

        [HttpPost]

    }
}
