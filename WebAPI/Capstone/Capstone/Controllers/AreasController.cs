using Capstone.Models;
using Capstone.Sdk;
using Capstone.ViewModels;
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

        /// <summary>
        /// 
        /// </summary>
        /// <param name="model">Contain areaId and number of empty slot</param>
        /// <returns>true/false</returns>
        [HttpPost]
        [Route("UpdateNumberOfEmptySlotMultiArea")]
        public IHttpActionResult UpdateNumberOfEmptySlotMultiArea(IEnumerable<AreaWithEmptySlot> model)
        {
            try
            {
                var areaApi = new AreaApi();
                foreach (var item in model)
                {
                    var area = areaApi.Get(item.AreaId);
                    if (area == null || area.Active == false)
                    {
                        
                    }
                    else
                    {
                        area.EmptyAmount = item.EmptyNumber;
                        areaApi.Edit(item.AreaId, area);
                    }
                }

                return Json(new ResultModel
                {
                    message = "Cập nhật thành công",
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

        [HttpGet]
        [Route("GetAreasByCarParkid/{carParkId}")]
        [ResponseType(typeof(List<AreaCustomViewModel>))]
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

        [HttpPost]
        [Route("UpdateName")]
        public IHttpActionResult UpdateName(AreaUpdateViewModel model)
        {
            try
            {
                var areaApi = new AreaApi();
                areaApi.UpdateName(model);
                return Json(new
                {
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
        [Route("UpdateStatus")]
        public IHttpActionResult UpdateStatus(AreaUpdateViewModel model)
        {
            try
            {
                var areaApi = new AreaApi();
                var result = areaApi.UpdateStatus(model);
                if (result)
                {
                    return Json(new ResultModel
                    {
                        message = "Cập nhập thành công",
                        success = true,
                    });
                }
                else
                {
                    return Json(new ResultModel
                    {
                        message = "Có chỗ đậu đang sử dụng, vui lòng cập nhật sau",
                        success = false,
                    });
                }
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
    }

    public class AreaWithEmptySlot
    {
        public int AreaId { get; set; }
        public int EmptyNumber { get; set; }
    }
}
