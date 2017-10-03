using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models.Entities.Services
{
    public partial interface IParkingLotService
    {
        IQueryable<ParkingLot> GetParkingLotsByCarParkId(int carParkId);
        IQueryable<ParkingLot> GetParkingLotsByAreaId(int areaId);
        void UpdateStatus(IEnumerable<ParkingLot> parkingLots, int status);
    }

    public partial class ParkingLotService
    {
        public IQueryable<ParkingLot> GetParkingLotsByCarParkId(int carParkId)
        {
            var result = this.GetActive(q => q.CarParkId == carParkId);
            return result;
        }

        public IQueryable<ParkingLot> GetParkingLotsByAreaId(int areaId)
        {
            var result = this.GetActive(q => q.AreaId == areaId);
            return result;
        }

        public void UpdateStatus(IEnumerable<ParkingLot> parkingLots, int status)
        {
            foreach (var item in parkingLots)
            {
                item.Status = status;
                this.Update(item);
            }
        }
    }
}