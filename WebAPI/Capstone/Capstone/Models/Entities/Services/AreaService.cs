using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models.Entities.Services
{
    public partial interface IAreaService
    {
        IQueryable<Area> GetAreaByCarParkId(int carParkId);
    }

    public partial class AreaService
    {
        public IQueryable<Area> GetAreaByCarParkId(int carParkId)
        {
            return this.GetActive(q => q.CarParkId == carParkId);
        }
    }
}