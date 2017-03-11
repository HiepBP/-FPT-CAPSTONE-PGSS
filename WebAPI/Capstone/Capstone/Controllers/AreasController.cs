using Capstone.Sdk;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;

namespace Capstone.Controllers
{
    [RoutePrefix("api/Areas")]
    public class AreasController : ApiController
    {
        /// <summary>
        /// Get the number of empty slot of selected area
        /// </summary>
        /// <param name="areaId">Area Id</param>
        /// <returns>Number of empty slot</returns>
        [HttpGet]
        [Route("GetNumberOfEmptySlot/{areaId}")]
        [ResponseType(typeof(int))]
        public IHttpActionResult GetNumberOfEmptySlot(int areaId)
        {
            try
            {
                var areaApi = new AreaApi();
                var area = areaApi.Get(areaId);
                if (area == null || area.Active == false)
                {
                    return Json(new
                    {
                        success = false,
                    });
                }
                else
                {
                    return Json(new
                    {
                        result = area.EmptyAmount,
                        success = true,
                    });
                }
            }
            catch (Exception ex)
            {
                return Json(new
                {
                    success = false,
                });
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="model">Contain areaId and number of empty slot</param>
        /// <returns>true/false</returns>
        [HttpPost]
        [Route("UpdateNumberOfEmptySlot")]
        public IHttpActionResult UpdateNumberOfEmptySlot(AreaWithEmptySlot model)
        {
            try
            {
                var areaApi = new AreaApi();
                var area = areaApi.Get(model.AreaId);
                if (area == null || area.Active == false)
                {
                    return Json(new
                    {
                        success = false,
                    });
                }
                else
                {
                    area.EmptyAmount = model.EmptyNumber;
                    areaApi.Edit(model.AreaId, area);
                    return Json(new
                    {
                        success = true,
                    });
                }
            }
            catch (Exception ex)
            {
                return Json(new
                {
                    success = false,
                });
            }
        }

        [HttpGet]
        [Route("GetAreasByCarParkid/{carParkId}")]
        [ResponseType(typeof(int))]
        public IHttpActionResult GetAreasByCarParkid(int carParkId)
        {
            try
            {
                var areaApi = new AreaApi();
                var listArea = areaApi.GetAreaByCarParkId(carParkId);
                return Json(new
                {
                    result = listArea,
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

    public class AreaWithEmptySlot
    {
        public int AreaId { get; set; }
        public int EmptyNumber { get; set; }
    }
}
