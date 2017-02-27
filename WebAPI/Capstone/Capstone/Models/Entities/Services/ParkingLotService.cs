using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models.Entities.Services
{
    public partial interface IParkingLotService
    {
        IQueryable<ParkingLot> GetParkingLotsByCarParkId(int carParkId);
    }

    public partial class ParkingLotService
    {
        public IQueryable<ParkingLot> GetParkingLotsByCarParkId(int carParkId)
        {
            var result = this.GetActive().Where(q => q.CarParkId == carParkId);
            return result;
        }
    }
}